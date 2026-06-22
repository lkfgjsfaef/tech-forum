package com.example.forum.api.model.vo.comment;

import lombok.Data;

@Data
public class CommentSaveReq {
    private Long articleId;
    private Long parentId;
    private String content;
    private String commentContent;
    private Long userId;
}
