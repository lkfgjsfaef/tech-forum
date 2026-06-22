package com.example.forum.api.model.vo.user;

import lombok.Data;

@Data
public class UserPwdLoginReq {
    private String username;
    private String password;
    private String nickname;
    private String email;
}
