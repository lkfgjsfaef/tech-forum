package com.example.forum.service.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRelationDO {
    private Long id;
    private Long userId;
    private Long followUserId;
    private Integer followState;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
