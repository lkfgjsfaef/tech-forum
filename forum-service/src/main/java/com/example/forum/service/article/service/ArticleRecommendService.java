package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;

import java.util.List;

public interface ArticleRecommendService {
    List<ArticleDTO> queryRecommendArticles(Long articleId, int size);
    List<ArticleDTO> queryHotArticles(int size);
    PageListVo<ArticleDTO> relatedRecommend(Long articleId, PageParam pageParam);
}
