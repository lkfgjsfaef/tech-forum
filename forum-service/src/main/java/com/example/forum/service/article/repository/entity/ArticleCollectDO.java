package com.example.forum.service.article.repository.entity;

import lombok.Data;

@Data
public class ArticleCollectDO {
    private Long id;
    private Long userId;
    private Long articleId;
}
