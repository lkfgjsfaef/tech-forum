package com.example.forum.service.article.repository.dao;

import com.example.forum.service.article.repository.entity.ArticleCollectDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleCollectDao {
    void insert(ArticleCollectDO collect);

    void delete(@Param("userId") Long userId, @Param("articleId") Long articleId);

    ArticleCollectDO findByUserAndArticle(@Param("userId") Long userId, @Param("articleId") Long articleId);

    List<ArticleCollectDO> findByUserId(@Param("userId") Long userId);

    int countByArticleId(@Param("articleId") Long articleId);

    boolean isCollectedByUser(@Param("userId") Long userId, @Param("articleId") Long articleId);
}
