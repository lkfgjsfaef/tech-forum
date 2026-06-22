package com.example.forum.service.comment.repository.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDO {
    private Long id;
    private Long articleId;
    private Long userId;
    private Long parentId;
    private String content;
    private Integer status;
    private Integer top;
    private Long createTime;
    private Long updateTime;
}
