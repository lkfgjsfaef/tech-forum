package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

@Data
public class ArticleOtherDTO {
    private Integer readType;
    private String preArticleTitle;
    private Long preArticleId;
    private String nextArticleTitle;
    private Long nextArticleId;
    private ColumnArticleFlipDTO flip;
}
