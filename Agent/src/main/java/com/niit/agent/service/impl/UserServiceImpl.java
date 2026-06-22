package com.niit.agent.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niit.agent.common.util.JwtUtil;
import com.niit.agent.entity.User;
import com.niit.agent.mapper.UserMapper;
import com.niit.agent.service.UserService;
import com.niit.agent.vo.LoginVO;
import com.niit.agent.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    // TODO: move to configuration
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/avatars/";

    @Override
    public Map<String, Object> login(LoginVO loginVO) {
        User user = getByUsername(loginVO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!DigestUtil.md5Hex(loginVO.getPassword()).equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        String role = user.getRole() != null ? user.getRole() : "user";
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), role);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        return result;
    }

    @Override
    public void register(LoginVO loginVO) {
        User exist = getByUsername(loginVO.getUsername());
        if (exist != null) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(loginVO.getUsername());
        user.setPassword(DigestUtil.md5Hex(loginVO.getPassword()));
        userMapper.insert(user);
    }

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public UserProfileVO getProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        UserProfileVO vo = new UserProfileVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public void updateProfile(Long userId, UserProfileVO profileVO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (StringUtils.hasText(profileVO.getNickname())) user.setNickname(profileVO.getNickname());
        if (StringUtils.hasText(profileVO.getBio())) user.setBio(profileVO.getBio());
        if (StringUtils.hasText(profileVO.getTheme())) user.setTheme(profileVO.getTheme());
        userMapper.updateById(user);
    }

    @Override
    public String updateAvatar(Long userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".png";
            String newFilename = UUID.randomUUID().toString().replace("-", "") + ext;
            File dest = new File(uploadDir + newFilename);
            file.transferTo(dest);
            
            // For simple demo, return a relative path. Need WebMvcConfigurer to serve static resources
            String avatarUrl = "/api/avatars/" + newFilename;
            user.setAvatarUrl(avatarUrl);
            userMapper.updateById(user);
            return avatarUrl;
        } catch (IOException e) {
            throw new RuntimeException("上传头像失败", e);
        }
    }

    @Override
    public void incrementTokenUsage(Long userId, int tokens) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setTotalTokens((user.getTotalTokens() != null ? user.getTotalTokens() : 0L) + tokens);
            userMapper.updateById(user);
        }
    }
}



