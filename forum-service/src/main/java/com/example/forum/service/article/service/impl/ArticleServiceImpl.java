package com.example.forum.service.article.service.impl;

import com.example.forum.service.article.entity.Article;
import com.example.forum.service.article.mapper.ArticleMapper;
import com.example.forum.service.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void create(Article article) {
        article.setViewCount(0);
        article.setCommentCount(0);
        article.setLikeCount(0);
        article.setStatus(1);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.insert(article);
    }

    @Override
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
    }

    @Override
    public Article findById(Long id) {
        return articleMapper.findById(id);
    }

    @Override
    public List<Article> findAll() {
        return articleMapper.findAll();
    }

    @Override
    public List<Article> findByUserId(Long userId) {
        return articleMapper.findByUserId(userId);
    }

    @Override
    public void incrementViewCount(Long id) {
        articleMapper.incrementViewCount(id);
    }

    @Override
    public void incrementCommentCount(Long id) {
        articleMapper.incrementCommentCount(id);
    }

    @Override
    public void incrementLikeCount(Long id) {
        articleMapper.incrementLikeCount(id);
    }
}
