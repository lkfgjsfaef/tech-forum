package com.example.forum.api.model.vo.comment.vo;

import lombok.Data;

import java.util.List;

@Data
public class SubCommentListVO {
    private Long parentId;
    private List<SubCommentItem> comments;
    private boolean hasMore;
    
    @Data
    public static class SubCommentItem {
        private Long id;
        private String content;
        private String userName;
        private Long createTime;
    }
}
