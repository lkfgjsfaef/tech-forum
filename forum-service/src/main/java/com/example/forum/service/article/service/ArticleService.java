package com.example.forum.service.article.service;

import com.example.forum.service.article.entity.Article;

import java.util.List;

public interface ArticleService {
    void create(Article article);
    void update(Article article);
    Article findById(Long id);
    List<Article> findAll();
    List<Article> findByUserId(Long userId);
    void incrementViewCount(Long id);
    void incrementCommentCount(Long id);
    void incrementLikeCount(Long id);
}
