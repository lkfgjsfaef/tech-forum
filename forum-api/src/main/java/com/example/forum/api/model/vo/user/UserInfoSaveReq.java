package com.example.forum.api.model.vo.user;

import lombok.Data;

@Data
public class UserInfoSaveReq {
    private Long userId;
    private String nickname;
    private String photo;
    private String avatar;
    private String email;
    private String profile;
}
