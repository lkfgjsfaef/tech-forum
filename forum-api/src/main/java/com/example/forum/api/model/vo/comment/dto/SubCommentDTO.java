package com.example.forum.api.model.vo.comment.dto;

import lombok.Data;

@Data
public class SubCommentDTO {
    private Long commentId;
    private Long parentId;
    private String content;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Integer likeCount;
}
