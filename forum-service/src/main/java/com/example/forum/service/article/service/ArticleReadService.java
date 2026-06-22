package com.example.forum.service.article.service;

import com.example.forum.api.model.enums.HomeSelectEnum;
import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.CategoryDTO;
import com.example.forum.api.model.vo.article.dto.SimpleArticleDTO;
import com.example.forum.service.article.repository.entity.ArticleDO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface ArticleReadService {
    ArticleDTO queryDetailArticle(Long articleId);
    ArticleDTO queryDetailArticleBySlug(String slug);
    ArticleDTO queryDetailArticleInfo(Long articleId);
    ArticleDTO queryFullArticleInfo(Long articleId, Long userId);
    PageListVo<ArticleDTO> queryArticles(PageParam pageParam);
    PageListVo<ArticleDTO> queryArticlesByCategory(Long categoryId, PageParam pageParam);
    PageListVo<ArticleDTO> queryArticlesByTag(Long tagId, PageParam pageParam);
    PageListVo<ArticleDTO> queryArticlesBySearchKey(String key, PageParam pageParam);
    List<ArticleDTO> queryTopArticlesByCategory(Long categoryId);
    Map<Long, Long> queryArticleCountsByCategory();
    List<ArticleDTO> queryRelatedArticles(Long articleId, int size);
    PageListVo<ArticleDTO> queryArticlesByUserAndType(Long userId, PageParam pageParam, HomeSelectEnum select);
    ArticleDO queryBasicArticle(Long articleId);
    String generateSummary(String content);
    List<SimpleArticleDTO> querySimpleArticleBySearchKey(String key);

    // 时间过滤查询
    PageListVo<ArticleDTO> queryArticlesByTime(Integer days, PageParam pageParam);
}
