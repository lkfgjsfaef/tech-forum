package com.example.forum.service.user.service;

import com.example.forum.api.model.vo.user.UserPwdLoginReq;
import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;

public interface LoginService {
    String SESSION_KEY = "forum_session";
    String USER_DEVICE_KEY = "forum_device";
    
    BaseUserInfoDTO login(String username, String password);
    void logout();
    BaseUserInfoDTO getCurrentUser();
    Long getCurrentUserId();
    String login(String username, String password, String clientIp);
    String loginByUserPwd(String username, String password);
    String registerByUserPwd(UserPwdLoginReq loginReq);
    void logout(String session);
}
