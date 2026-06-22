package com.example.forum.service.user.service;

import com.example.forum.api.model.vo.user.UserInfoSaveReq;
import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import com.example.forum.api.model.vo.user.dto.SimpleUserInfoDTO;
import com.example.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import com.example.forum.service.user.entity.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User findByEmail(String email);
    void register(User user);
    User login(String username, String password);
    User findById(Long id);
    void update(User user);
    BaseUserInfoDTO getAndUpdateUserIpInfoBySessionId(String session, String clientIp);
    BaseUserInfoDTO queryBasicUserInfo(Long userId);
    UserStatisticInfoDTO queryUserInfoWithStatistic(Long userId);
    void saveUserInfo(UserInfoSaveReq req);
    List<SimpleUserInfoDTO> searchUser(String key);
    void changePassword(Long userId, String oldPassword, String newPassword);
}
