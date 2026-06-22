package com.example.forum.service.article.repository.dao;

import com.example.forum.service.article.repository.entity.ColumnArticleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ColumnArticleDao {
    List<ColumnArticleDO> findByColumnId(@Param("columnId") Long columnId);

    ColumnArticleDO findByColumnIdAndSection(@Param("columnId") Long columnId, @Param("section") Integer section);

    ColumnArticleDO findByArticleId(@Param("articleId") Long articleId);
    
    void insert(ColumnArticleDO columnArticle);

    Integer getMaxSection(@Param("columnId") Long columnId);

    void deleteByColumnAndArticle(@Param("columnId") Long columnId, @Param("articleId") Long articleId);

    ColumnArticleDO findByColumnAndArticle(@Param("columnId") Long columnId, @Param("articleId") Long articleId);
}
