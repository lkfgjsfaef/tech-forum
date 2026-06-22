package com.example.forum.api.model.vo.user.dto;

import lombok.Data;

@Data
public class FollowUserInfoDTO {
    private Long userId;
    private String userName;
    private String nickName;
    private String photo;
    private String signature;
    private Boolean followed;
}
