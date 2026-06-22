package com.example.forum.api.model.vo.comment;

import lombok.Data;

@Data
public class SearchCommentReq {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long articleId;
    private String content;
}
