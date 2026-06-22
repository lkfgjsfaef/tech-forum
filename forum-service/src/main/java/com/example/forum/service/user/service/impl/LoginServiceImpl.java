package com.example.forum.service.user.service.impl;

import com.example.forum.api.model.vo.user.UserPwdLoginReq;
import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import com.example.forum.service.user.entity.User;
import com.example.forum.service.user.mapper.UserInfoMapper;
import com.example.forum.service.user.mapper.UserMapper;
import com.example.forum.service.user.service.LoginService;
import com.example.forum.service.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginServiceImpl implements LoginService {
    
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserService userService;

    /**
     * 验证码存储（手机册"邮箱 -> 验证码）
     * 生产环境应使册"Redis，这里用内存 Map 演示
     */
    private static final Map<String, String> VERIFY_CODE_MAP = new ConcurrentHashMap<>();
    private static final Random RANDOM = new Random();

    /**
     * MD5加密
     */
    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * 生成6位数字验证码
     */
    public String generateVerifyCode(String target) {
        String code = String.format("%06d", RANDOM.nextInt(1000000));
        VERIFY_CODE_MAP.put(target, code);
        // 模拟发送验证码，仅打印到控制台（生产环境应接入真实邮件/短信服务册"
        log.info("【模拟发送验证码】target={}, code={}", target, code);
        return code;
    }

    /**
     * 验证验证码是否正册"
     */
    public boolean verifyCode(String target, String code) {
        String storedCode = VERIFY_CODE_MAP.get(target);
        if (storedCode != null && storedCode.equals(code)) {
            VERIFY_CODE_MAP.remove(target);
            return true;
        }
        return false;
    }

    @Override
    public BaseUserInfoDTO login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            user = userMapper.findByEmail(username);
        }
        if (user != null && md5(password).equals(user.getPassword())) {
            return userService.queryBasicUserInfo(user.getId());
        }
        return null;
    }

    @Override
    public void logout() {
    }

    @Override
    public BaseUserInfoDTO getCurrentUser() {
        return null;
    }

    @Override
    public Long getCurrentUserId() {
        return null;
    }

    @Override
    public String login(String username, String password, String clientIp) {
        return loginByUserPwd(username, password);
    }

    @Override
    public String loginByUserPwd(String username, String password) {
        // 先按用户名查，再按邮箱查
        User user = userMapper.findByUsername(username);
        if (user == null) {
            user = userMapper.findByEmail(username);
        }
        if (user != null && md5(password).equals(user.getPassword())) {
            String session = UUID.randomUUID().toString().replace("-", "");
            user.setSession(session);
            user.setUpdateTime(LocalDateTime.now());
            userMapper.update(user);
            return session;
        }
        return null;
    }

    /**
     * 检查用户是否存在（用于登录时区册"账号不存在"册"密码错误"册"
     */
    public User checkUserExists(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            user = userMapper.findByEmail(username);
        }
        return user;
    }

    /**
     * 验证码登录（通过邮箱或手机号册"
     * 不会自动注册，用户不存在则返回null
     */
    public String loginByVerifyCode(String target, String code) {
        if (!verifyCode(target, code)) {
            return null;
        }
        // 先按用户名查，再按邮箱查（user_info.email册"
        User user = userMapper.findByUsername(target);
        if (user == null) {
            user = userMapper.findByEmail(target);
        }
        // 验证码登录不自动注册，用户不存在则登录失败"
        if (user == null) {
            return null;
        }
        String session = UUID.randomUUID().toString().replace("-", "");
        user.setSession(session);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
        return session;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String registerByUserPwd(UserPwdLoginReq loginReq) {
        User existUser = userMapper.findByUsername(loginReq.getUsername());
        if (existUser != null) {
            return null;
        }
        // 检查邮箱是否已被使册"
        if (loginReq.getEmail() != null && !loginReq.getEmail().isEmpty()) {
            int emailCount = userInfoMapper.countByEmail(loginReq.getEmail());
            if (emailCount > 0) {
                return "EMAIL_EXISTS";
            }
        }
        User user = new User();
        user.setUsername(loginReq.getUsername());
        user.setPassword(md5(loginReq.getPassword()));
        user.setNickname(loginReq.getNickname());
        user.setEmail(loginReq.getEmail());
        user.setRole(0);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);

        // 同步写入 user_info 册"
        String photo = user.getAvatar() != null && !user.getAvatar().isEmpty() 
                ? user.getAvatar() : "";
        userInfoMapper.insert(user.getId(), loginReq.getUsername(), 
                loginReq.getNickname() != null ? loginReq.getNickname() : loginReq.getUsername(), 
                photo,
                loginReq.getEmail() != null ? loginReq.getEmail() : "");

        String session = UUID.randomUUID().toString().replace("-", "");
        user.setSession(session);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
        return session;
    }

    @Override
    public void logout(String session) {
        if (session == null || session.isEmpty()) {
            return;
        }
        User user = userMapper.findBySession(session);
        if (user != null) {
            user.setSession(null);
            user.setUpdateTime(LocalDateTime.now());
            userMapper.update(user);
        }
    }
}


