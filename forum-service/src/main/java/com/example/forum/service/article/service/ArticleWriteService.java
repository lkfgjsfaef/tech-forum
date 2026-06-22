package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.article.ArticlePostReq;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import org.springframework.stereotype.Service;

@Service
public interface ArticleWriteService {
    Long saveArticle(ArticleDTO articleDTO);
    Long saveArticle(ArticlePostReq req, Long userId);
    void updateArticle(ArticleDTO articleDTO);
    void deleteArticle(Long articleId);
    void deleteArticle(Long articleId, Long userId);
    void publishArticle(Long articleId);
    void offlineArticle(Long articleId);

    void incrementViewCount(Long articleId);
}
