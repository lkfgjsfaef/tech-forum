package com.example.forum.api.model.vo.user.dto;

import lombok.Data;

@Data
public class SimpleUserInfoDTO {
    private Long userId;
    private String userName;
    private String photo;
}
