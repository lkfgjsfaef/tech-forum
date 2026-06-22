package com.example.forum.api.model.vo.comment.dto;

import lombok.Data;

import java.util.List;

@Data
public class TopCommentDTO {
    private Long id;
    private Long articleId;
    private Long userId;
    private String userName;
    private String userAvatar;
    private String content;
    private Integer likeCount;
    private Long createTime;
    private List<SubCommentDTO> subComments;
    
    @Data
    public static class SubCommentDTO {
        private Long id;
        private Long parentId;
        private Long userId;
        private String userName;
        private String userAvatar;
        private String content;
        private Long createTime;
    }
}
