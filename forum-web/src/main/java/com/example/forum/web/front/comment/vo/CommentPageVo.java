package com.example.forum.web.front.comment.vo;

import lombok.Data;

/**
 * 评论分页渲染结果
 */
@Data
public class CommentPageVo {
    /**
     * 评论片段 html
     */
    private String html;

    /**
     * 是否还有更多一级评册"
     */
    private Boolean hasMore;

    /**
     * 下一页页册"
     */
    private Long nextPageNum;
}

