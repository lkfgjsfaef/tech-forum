package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.enums.HomeSelectEnum;
import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.CategoryDTO;
import com.example.forum.api.model.vo.article.dto.SimpleArticleDTO;
import com.example.forum.service.article.entity.Article;
import com.example.forum.service.article.mapper.ArticleMapper;
import com.example.forum.service.article.repository.entity.ArticleDO;
import com.example.forum.service.article.service.ArticleCollectService;
import com.example.forum.service.article.service.ArticleReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleReadServiceImpl implements ArticleReadService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleCollectService articleCollectService;

    @Override
    @Cacheable(value = "articleDetail", key = "#articleId", unless = "#result == null")
    public ArticleDTO queryDetailArticle(Long articleId) {
        if (articleId == null) {
            return null;
        }
        return articleMapper.queryArticleDetail(articleId);
    }

    @Override
    public ArticleDTO queryDetailArticleBySlug(String slug) {
        return null;
    }

    @Override
    public ArticleDTO queryDetailArticleInfo(Long articleId) {
        return queryDetailArticle(articleId);
    }

    @Override
    public ArticleDTO queryFullArticleInfo(Long articleId, Long userId) {
        return queryDetailArticle(articleId);
    }

    @Override
    public PageListVo<ArticleDTO> queryArticles(PageParam pageParam) {
        if (pageParam == null) {
            pageParam = PageParam.newPageInstance();
        }
        List<ArticleDTO> list = articleMapper.queryArticles(pageParam.getOffset(), pageParam.getPageSize());
        long total = articleMapper.countTotal();
        return PageListVo.of(list, total, pageParam.getPageNum(), pageParam.getPageSize());
    }

    @Override
    public PageListVo<ArticleDTO> queryArticlesByCategory(Long categoryId, PageParam pageParam) {
        if (pageParam == null) {
            pageParam = PageParam.newPageInstance();
        }
        List<ArticleDTO> list = articleMapper.queryArticlesByCategory(
                categoryId, pageParam.getOffset(), pageParam.getPageSize());
        long total = articleMapper.countTotalByCategory(categoryId);

        PageListVo<ArticleDTO> result = PageListVo.of(list, total, pageParam.getPageNum(), pageParam.getPageSize());
        return result;
    }

    @Override
    public PageListVo<ArticleDTO> queryArticlesByTag(Long tagId, PageParam pageParam) {
        if (pageParam == null) {
            pageParam = PageParam.newPageInstance();
        }
        List<ArticleDTO> list = articleMapper.queryArticlesByTag(tagId, pageParam.getOffset(), pageParam.getPageSize());
        long total = articleMapper.countByTag(tagId);
        return PageListVo.of(list, total, pageParam.getPageNum(), pageParam.getPageSize());
    }

    @Override
    public PageListVo<ArticleDTO> queryArticlesBySearchKey(String key, PageParam pageParam) {
        if (pageParam == null) {
            pageParam = PageParam.newPageInstance();
        }
        if (key == null || key.trim().isEmpty()) {
            return PageListVo.emptyVo();
        }
        List<ArticleDTO> list = articleMapper.queryArticlesBySearchKey(
                key.trim(), pageParam.getOffset(), pageParam.getPageSize());
        long total = articleMapper.countBySearchKey(key.trim());
        return PageListVo.of(list, total, pageParam.getPageNum(), pageParam.getPageSize());
    }

    @Override
    public List<ArticleDTO> queryTopArticlesByCategory(Long categoryId) {
        return articleMapper.queryTopArticles(categoryId, PageParam.TOP_PAGE_SIZE);
    }

    @Override
    @Cacheable(value = "articleCountsByCategory", key = "'all'")
    public Map<Long, Long> queryArticleCountsByCategory() {
        List<Map<String, Object>> list = articleMapper.countArticlesByCategory();
        Map<Long, Long> result = new HashMap<>();
        if (list != null) {
            for (Map<String, Object> row : list) {
                Long categoryId = ((Number) row.get("categoryId")).longValue();
                Long count = ((Number) row.get("cnt")).longValue();
                result.put(categoryId, count);
            }
        }
        return result;
    }

    @Override
    public List<ArticleDTO> queryRelatedArticles(Long articleId, int size) {
        if (articleId == null || size <= 0) {
            return new ArrayList<>();
        }
        return articleMapper.queryRelatedArticles(articleId, size);
    }

    @Override
    public PageListVo<ArticleDTO> queryArticlesByUserAndType(Long userId, PageParam pageParam, HomeSelectEnum select) {
        if (userId == null) {
            return PageListVo.emptyVo();
        }
        switch (select) {
            case ARTICLE:
                // 查询用户的文章列册"
                List<Article> articles = articleMapper.findByUserId(userId);
                List<ArticleDTO> dtoList = articles.stream().map(article -> {
                    ArticleDTO dto = new ArticleDTO();
                    dto.setArticleId(article.getId());
                    dto.setTitle(article.getTitle());
                    dto.setSummary(article.getSummary());
                    dto.setAuthorId(article.getUserId());
                    dto.setCreateTime(article.getCreateTime());
                    dto.setReadCount(0);
                    return dto;
                }).collect(Collectors.toList());
                // 简单分册"
                int total = dtoList.size();
                int fromIndex = (int) Math.min(pageParam.getOffset(), total);
                int toIndex = (int) Math.min(pageParam.getOffset() + pageParam.getPageSize(), total);
                List<ArticleDTO> pageList = dtoList.subList(fromIndex, toIndex);
                return PageListVo.of(pageList, (long) total, pageParam.getPageNum(), pageParam.getPageSize());
            case COLLECT:
                return articleCollectService.queryUserCollections(userId, pageParam);
            case READ:
            case LIKE:
            case COMMENT:
            default:
                return PageListVo.emptyVo();
        }
    }

    @Override
    public ArticleDO queryBasicArticle(Long articleId) {
        if (articleId == null) {
            return null;
        }
        Article article = articleMapper.findById(articleId);
        if (article == null) {
            return null;
        }
        ArticleDO entity = new ArticleDO();
        entity.setId(article.getId());
        entity.setTitle(article.getTitle());
        entity.setSummary(article.getSummary());
        entity.setUserId(article.getUserId());
        entity.setStatus(article.getStatus());
        entity.setCreateTime(article.getCreateTime());
        entity.setUpdateTime(article.getUpdateTime());
        return entity;
    }

    @Override
    public String generateSummary(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        return content.length() > 300 ? content.substring(0, 300) + "..." : content;
    }

    @Override
    public List<SimpleArticleDTO> querySimpleArticleBySearchKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<ArticleDTO> list = articleMapper.queryArticlesBySearchKey(key.trim(), 0, 20);
        return list.stream().map(a -> {
            SimpleArticleDTO dto = new SimpleArticleDTO();
            dto.setId(a.getArticleId());
            dto.setTitle(a.getTitle());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public PageListVo<ArticleDTO> queryArticlesByTime(Integer days, PageParam pageParam) {
        if (pageParam == null) {
            pageParam = PageParam.newPageInstance();
        }
        List<ArticleDTO> list = articleMapper.queryArticlesByTime(days, pageParam.getOffset(), pageParam.getPageSize());
        long total = articleMapper.countTotalByTime(days);
        return PageListVo.of(list, total, pageParam.getPageNum(), pageParam.getPageSize());
    }
}

