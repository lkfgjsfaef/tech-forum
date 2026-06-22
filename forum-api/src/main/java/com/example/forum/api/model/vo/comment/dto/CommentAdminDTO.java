package com.example.forum.api.model.vo.comment.dto;

import lombok.Data;

@Data
public class CommentAdminDTO {
    private Long id;
    private Long articleId;
    private String articleTitle;
    private Long userId;
    private String userName;
    private String content;
    private Integer status;
    private Long createTime;
}
