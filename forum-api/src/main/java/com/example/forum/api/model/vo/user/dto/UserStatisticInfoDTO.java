package com.example.forum.api.model.vo.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserStatisticInfoDTO {
    private Long userId;
    private String userName;
    private String photo;
    private String profile;
    private Integer articleCount;
    private Integer readCount;
    private Integer followCount;
    private Integer fansCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    private Integer followerCount;  // 粉丝数量（与fansCount同义册"
    private Boolean followed;       // 当前登录用户是否已关注该用户
    private String payCode;
    private List<PayCodeDTO> payQrCodes;
}

