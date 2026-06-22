package com.example.forum.service.comment.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private Long topCommentId;
    private Long parentCommentId;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
