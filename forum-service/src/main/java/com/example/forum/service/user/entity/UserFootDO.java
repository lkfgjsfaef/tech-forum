package com.example.forum.service.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserFootDO {
    private Long id;
    private Long userId;
    private Long documentId;
    private Integer documentType;
    private Long documentUserId;
    private Integer collectionStat;
    private Integer readStat;
    private Integer commentStat;
    private Integer praiseStat;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
