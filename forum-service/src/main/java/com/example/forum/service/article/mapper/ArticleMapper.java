package com.example.forum.service.article.mapper;

import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.service.article.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper {
    void insert(Article article);
    void update(Article article);
    Article findById(@Param("id") Long id);
    List<Article> findAll();
    List<Article> findByUserId(@Param("userId") Long userId);
    void incrementViewCount(@Param("id") Long id);
    void incrementCommentCount(@Param("id") Long id);
    void incrementLikeCount(@Param("id") Long id);

    List<ArticleDTO> queryArticles(@Param("offset") long offset, @Param("pageSize") int pageSize);

    long countTotal();

    List<ArticleDTO> queryArticlesByCategory(@Param("categoryId") Long categoryId,
                                             @Param("offset") long offset,
                                             @Param("pageSize") int pageSize);

    List<Map<String, Object>> countArticlesByCategory();

    long countTotalByCategory(@Param("categoryId") Long categoryId);

    List<ArticleDTO> queryTopArticles(@Param("categoryId") Long categoryId, @Param("limit") int limit);

    ArticleDTO queryArticleDetail(@Param("articleId") Long articleId);

    List<ArticleDTO> queryArticlesBySearchKey(@Param("key") String key,
                                               @Param("offset") long offset,
                                               @Param("pageSize") int pageSize);

    long countBySearchKey(@Param("key") String key);

    List<ArticleDTO> queryArticlesByTag(@Param("tagId") Long tagId,
                                       @Param("offset") long offset,
                                       @Param("pageSize") int pageSize);

    long countByTag(@Param("tagId") Long tagId);

    List<ArticleDTO> queryRelatedArticles(@Param("articleId") Long articleId, @Param("size") int size);

    // 时间过滤查询方法
    List<ArticleDTO> queryArticlesByTime(@Param("days") Integer days,
                                         @Param("offset") long offset,
                                         @Param("pageSize") int pageSize);

    long countTotalByTime(@Param("days") Integer days);

    // 统计已发布文章总数（用于站点统计）
    long countAll();

    // 统计某作者已发布的文章数册"
    long countByAuthorId(@Param("authorId") Long authorId);
}

