package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.article.ArticlePostReq;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.core.aop.OperationLog;
import com.example.forum.service.article.entity.Article;
import com.example.forum.service.article.mapper.ArticleDetailMapper;
import com.example.forum.service.article.mapper.ArticleMapper;
import com.example.forum.service.article.repository.dao.ColumnArticleDao;
import com.example.forum.service.article.repository.entity.ColumnArticleDO;
import com.example.forum.service.article.service.ArticleWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ArticleWriteServiceImpl implements ArticleWriteService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleDetailMapper articleDetailMapper;
    
    @Autowired
    private ColumnArticleDao columnArticleDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"articleCountsByCategory"}, key = "'all'")
    public Long saveArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setShortTitle(articleDTO.getShortTitle());
        article.setSummary(articleDTO.getSummary());
        article.setContent(articleDTO.getContent());
        article.setCover(articleDTO.getCover());
        article.setUserId(Long.valueOf(articleDTO.getAuthor()));
        article.setCategoryId(articleDTO.getCategoryId());
        article.setSourceType(articleDTO.getSourceType());
        article.setSourceUrl(articleDTO.getSourceUrl());
        article.setStatus(1);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        
        articleMapper.insert(article);
        
        // 保存文章详情
        if (article.getId() != null && articleDTO.getContent() != null) {
            articleDetailMapper.insertArticleDetail(article.getId(), articleDTO.getContent());
        }
        
        return article.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"articleCountsByCategory"}, key = "'all'")
    @OperationLog(value = "发布文章", module = "文章管理")
    public Long saveArticle(ArticlePostReq req, Long userId) {
        Article article = new Article();
        article.setTitle(req.getTitle());
        article.setShortTitle(req.getShortTitle() != null ? req.getShortTitle() : req.getTitle());
        article.setSummary(req.getSummary() != null ? req.getSummary() : "");
        article.setCover(req.getCover() != null ? req.getCover() : "");
        article.setUserId(userId);
        article.setCategoryId(req.getCategoryId());
        article.setSourceType(req.getSourceType() != null ? req.getSourceType() : 2);
        article.setSourceUrl(req.getSourceUrl() != null ? req.getSourceUrl() : "");
        article.setStatus(1);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        
        articleMapper.insert(article);
        
        // 保存文章详情
        if (article.getId() != null && req.getContent() != null) {
            articleDetailMapper.insertArticleDetail(article.getId(), req.getContent());
        }
        
        // 如果选择了专栏，创建文章-专栏关联
        if (req.getColumnId() != null && req.getColumnId() > 0) {
            ColumnArticleDO columnArticle = new ColumnArticleDO();
            columnArticle.setColumnId(req.getColumnId());
            columnArticle.setArticleId(article.getId());
            columnArticle.setSection(0); // 默认值"册"
            columnArticle.setReadType(0); // 默认免费
            columnArticle.setStatus(0); // 默认正常状册"
            columnArticleDao.insert(columnArticle);
        }
        
        return article.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = "articleDetail", key = "#articleDTO.articleId"),
            @CacheEvict(value = "articleCountsByCategory", allEntries = true)
    })
    @OperationLog(value = "更新文章", module = "文章管理")
    public void updateArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setId(articleDTO.getArticleId());
        article.setTitle(articleDTO.getTitle());
        article.setShortTitle(articleDTO.getShortTitle());
        article.setSummary(articleDTO.getSummary());
        article.setCover(articleDTO.getCover());
        article.setCategoryId(articleDTO.getCategoryId());
        article.setUpdateTime(LocalDateTime.now());
        
        articleMapper.update(article);
        
        // 更新文章详情
        if (articleDTO.getContent() != null) {
            articleDetailMapper.updateArticleDetail(articleDTO.getArticleId(), articleDTO.getContent());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = "articleDetail", key = "#articleId"),
            @CacheEvict(value = "articleCountsByCategory", allEntries = true)
    })
    @OperationLog(value = "删除文章", module = "文章管理")
    public void deleteArticle(Long articleId) {
        // 软删除文册"
        Article article = articleMapper.findById(articleId);
        if (article != null) {
            article.setStatus(0); // 设置为未发布状册"
            article.setUpdateTime(LocalDateTime.now());
            articleMapper.update(article);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = "articleDetail", key = "#articleId"),
            @CacheEvict(value = "articleCountsByCategory", allEntries = true)
    })
    @OperationLog(value = "删除文章（用户）", module = "文章管理")
    public void deleteArticle(Long articleId, Long userId) {
        // 验证是否是文章作册"
        Article article = articleMapper.findById(articleId);
        if (article != null && article.getUserId().equals(userId)) {
            article.setStatus(0);
            article.setUpdateTime(LocalDateTime.now());
            articleMapper.update(article);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = "articleDetail", key = "#articleId"),
            @CacheEvict(value = "articleCountsByCategory", allEntries = true)
    })
    @OperationLog(value = "发布文章", module = "文章管理")
    public void publishArticle(Long articleId) {
        Article article = articleMapper.findById(articleId);
        if (article != null) {
            article.setStatus(1);
            article.setUpdateTime(LocalDateTime.now());
            articleMapper.update(article);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = "articleDetail", key = "#articleId"),
            @CacheEvict(value = "articleCountsByCategory", allEntries = true)
    })
    @OperationLog(value = "下线文章", module = "文章管理")
    public void offlineArticle(Long articleId) {
        Article article = articleMapper.findById(articleId);
        if (article != null) {
            article.setStatus(0);
            article.setUpdateTime(LocalDateTime.now());
            articleMapper.update(article);
        }
    }

    @Override
    public void incrementViewCount(Long articleId) {
        articleMapper.incrementViewCount(articleId);
    }
}


