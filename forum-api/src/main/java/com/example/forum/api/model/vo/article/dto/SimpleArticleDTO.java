package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

@Data
public class SimpleArticleDTO {
    private Long id;
    private Long articleId;
    private String title;
    private String shortTitle;
    private String summary;
    private String cover;
    private Integer section;
}
