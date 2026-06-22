package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;

import java.util.List;
import java.util.Map;

public interface ArticleCollectService {
    void collect(Long userId, Long articleId);
    void uncollect(Long userId, Long articleId);
    boolean isCollected(Long userId, Long articleId);
    int getCollectCount(Long articleId);
    List<Long> getUserCollectedArticleIds(Long userId);
    PageListVo<ArticleDTO> queryUserCollections(Long userId, PageParam pageParam);
}
