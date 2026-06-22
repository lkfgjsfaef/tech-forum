package com.example.forum.api.model.vo.article;

import lombok.Data;

@Data
public class ColumnArticleReq {
    private Long id;
    private Long columnId;
    private Long articleId;
    private Long groupId;
    private Integer sort;
}
