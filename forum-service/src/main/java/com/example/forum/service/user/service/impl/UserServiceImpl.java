package com.example.forum.service.user.service.impl;

import com.example.forum.api.model.vo.user.UserInfoSaveReq;
import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import com.example.forum.api.model.vo.user.dto.SimpleUserInfoDTO;
import com.example.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import com.example.forum.service.user.entity.User;
import com.example.forum.service.user.mapper.UserFootMapper;
import com.example.forum.service.user.mapper.UserInfoMapper;
import com.example.forum.service.user.mapper.UserMapper;
import com.example.forum.service.user.mapper.UserRelationMapper;
import com.example.forum.service.article.mapper.ArticleMapper;
import com.example.forum.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRelationMapper userRelationMapper;

    @Autowired
    private UserFootMapper userFootMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public void register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(1);
        user.setRole(0);
        userMapper.insert(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            user = userMapper.findByEmail(username);
        }
        if (user != null && md5(password).equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public BaseUserInfoDTO getAndUpdateUserIpInfoBySessionId(String session, String clientIp) {
        if (session == null || session.isEmpty()) {
            return null;
        }
        User user = userMapper.findBySession(session);
        if (user == null) {
            return null;
        }
        // 更新最后登录时间（user表没有last_login_ip列，跳过IP更新）
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
        return convertToDTO(user);
    }

    private BaseUserInfoDTO convertToDTO(User user) {
        BaseUserInfoDTO dto = new BaseUserInfoDTO();
        dto.setUserId(user.getId());
        // 从 user_info 表读取昵称作为显示名，没有则用用户名
        String nickName = userInfoMapper.queryNickNameByUserId(user.getId());
        String displayName = (nickName != null && !nickName.isEmpty()) ? nickName : user.getUsername();
        dto.setUserName(displayName);
        dto.setNickName(nickName);
        dto.setPhoto(user.getAvatar());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    @Override
    public BaseUserInfoDTO queryBasicUserInfo(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = userMapper.findById(userId);
        return user != null ? convertToDTO(user) : null;
    }

    @Override
    public UserStatisticInfoDTO queryUserInfoWithStatistic(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = userMapper.findById(userId);
        if (user == null) {
            return null;
        }
        UserStatisticInfoDTO dto = new UserStatisticInfoDTO();
        dto.setUserId(user.getId());
        // 册"user_info 表读取昵称作为显示名
        String nickName = userInfoMapper.queryNickNameByUserId(user.getId());
        String displayName = (nickName != null && !nickName.isEmpty()) ? nickName : user.getUsername();
        dto.setUserName(displayName);
        dto.setPhoto(user.getAvatar());
        dto.setProfile(null);

        // 文章数 - 统计该作者已发布的文章数量
        dto.setArticleCount((int) articleMapper.countByAuthorId(user.getId()));
        // 阅读量"
        dto.setReadCount(0);
        // 关注数
        dto.setFollowCount(userRelationMapper.countFollows(userId));
        // 粉丝数
        int fansCount = userRelationMapper.countFans(userId);
        dto.setFansCount(fansCount);
        dto.setFollowerCount(fansCount);
        // 收到的点赞数 (别人点赞自己发布的文章)
        dto.setLikeCount(userFootMapper.countReceivedPraise(userId, 1));
        // 评论数
        dto.setCommentCount(0);
        // 收藏数
        dto.setCollectCount(userFootMapper.countUserCollection(userId, 1));
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserInfo(UserInfoSaveReq req) {
        if (req == null || req.getUserId() == null) {
            return;
        }
        // 更新 user_info 表（昵称、简介等）
        if (req.getNickname() != null || req.getProfile() != null) {
            String currentNick = userInfoMapper.queryNickNameByUserId(req.getUserId());
            String newNick = (req.getNickname() != null) ? req.getNickname() : currentNick;
            String profile = (req.getProfile() != null) ? req.getProfile() : "";
            // 通过 UPDATE 语句直接更新 user_info
            int rows = userInfoMapper.updateUserInfo(req.getUserId(), newNick, 
                    req.getAvatar() != null ? req.getAvatar() : "", 
                    profile);
            if (rows > 0) {
                log.info("Updated userId={} user_info: nick={}, avatar={}, profile={}",
                    req.getUserId(), newNick, req.getAvatar(), profile);
            }
        }
    }

    @Override
    public List<SimpleUserInfoDTO> searchUser(String key) {
        if (key == null || key.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<User> users = userMapper.searchUser(key.trim());
        List<SimpleUserInfoDTO> result = new ArrayList<>();
        for (User user : users) {
            SimpleUserInfoDTO dto = new SimpleUserInfoDTO();
            dto.setUserId(user.getId());
            // 从 user_info 读取昵称作为显示名
            String nickName = userInfoMapper.queryNickNameByUserId(user.getId());
            dto.setUserName((nickName != null && !nickName.isEmpty()) ? nickName : user.getUsername());
            dto.setPhoto(user.getAvatar());
            result.add(dto);
        }
        return result;
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!md5(oldPassword).equals(user.getPassword())) {
            throw new RuntimeException("原密码不正确");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("新密码长度不能少于6位");
        }
        user.setPassword(md5(newPassword));
        userMapper.update(user);
        log.info("用户修改密码成功: userId={}", userId);
    }
}


