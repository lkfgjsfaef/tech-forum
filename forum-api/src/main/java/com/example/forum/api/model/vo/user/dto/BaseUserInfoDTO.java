package com.example.forum.api.model.vo.user.dto;

import lombok.Data;

@Data
public class BaseUserInfoDTO {
    private Long userId;
    private String userName;
    private String nickName;
    private String photo;
    private String email;
    private Integer role;
    private Integer starStatus;
    private String ip;
    private String profile;
}
