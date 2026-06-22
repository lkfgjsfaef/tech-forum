package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.service.article.mapper.ArticleMapper;
import com.example.forum.service.article.repository.dao.ArticleCollectDao;
import com.example.forum.service.article.repository.entity.ArticleCollectDO;
import com.example.forum.service.article.service.ArticleCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleCollectServiceImpl implements ArticleCollectService {

    @Autowired
    private ArticleCollectDao articleCollectDao;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void collect(Long userId, Long articleId) {
        if (userId == null || articleId == null) return;
        ArticleCollectDO existing = articleCollectDao.findByUserAndArticle(userId, articleId);
        if (existing == null) {
            ArticleCollectDO collect = new ArticleCollectDO();
            collect.setUserId(userId);
            collect.setArticleId(articleId);
            articleCollectDao.insert(collect);
        }
    }

    @Override
    public void uncollect(Long userId, Long articleId) {
        if (userId == null || articleId == null) return;
        articleCollectDao.delete(userId, articleId);
    }

    @Override
    public boolean isCollected(Long userId, Long articleId) {
        if (userId == null || articleId == null) return false;
        return articleCollectDao.isCollectedByUser(userId, articleId);
    }

    @Override
    public int getCollectCount(Long articleId) {
        if (articleId == null) return 0;
        return articleCollectDao.countByArticleId(articleId);
    }

    @Override
    public List<Long> getUserCollectedArticleIds(Long userId) {
        List<ArticleCollectDO> collects = articleCollectDao.findByUserId(userId);
        if (collects == null || collects.isEmpty()) return new ArrayList<>();
        return collects.stream().map(ArticleCollectDO::getArticleId).collect(Collectors.toList());
    }

    @Override
    public PageListVo<ArticleDTO> queryUserCollections(Long userId, PageParam pageParam) {
        if (pageParam == null) pageParam = PageParam.newPageInstance();
        List<ArticleCollectDO> collects = articleCollectDao.findByUserId(userId);
        if (collects == null || collects.isEmpty()) {
            return PageListVo.emptyVo();
        }
        List<Long> articleIds = collects.stream()
                .map(ArticleCollectDO::getArticleId)
                .collect(Collectors.toList());

        int total = articleIds.size();
        int fromIndex = (int) Math.min(pageParam.getOffset(), total);
        int toIndex = (int) Math.min(pageParam.getOffset() + pageParam.getPageSize(), total);
        List<Long> pageIds = articleIds.subList(fromIndex, toIndex);

        List<ArticleDTO> articles = new ArrayList<>();
        for (Long aid : pageIds) {
            ArticleDTO dto = articleMapper.queryArticleDetail(aid);
            if (dto != null) articles.add(dto);
        }

        return PageListVo.of(articles, (long) total, pageParam.getPageNum(), pageParam.getPageSize());
    }
}
