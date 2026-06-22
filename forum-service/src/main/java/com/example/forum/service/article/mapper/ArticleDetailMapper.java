package com.example.forum.service.article.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章详情Mapper
 */
@Mapper
public interface ArticleDetailMapper {

    /**
     * 保存文章详情
     *
     * @param articleId 文章ID
     * @param content   文章内容(Markdown格式)
     */
    void insertArticleDetail(@Param("articleId") Long articleId, @Param("content") String content);

    /**
     * 更新文章详情
     *
     * @param articleId 文章ID
     * @param content   文章内容(Markdown格式)
     */
    void updateArticleDetail(@Param("articleId") Long articleId, @Param("content") String content);

    /**
     * 获取文章详情
     *
     * @param articleId 文章ID
     * @return 文章内容
     */
    String getArticleContent(@Param("articleId") Long articleId);
}
