package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ColumnDTO;
import com.example.forum.api.model.vo.article.dto.SimpleArticleDTO;
import com.example.forum.service.article.entity.Article;
import com.example.forum.service.article.repository.entity.ColumnArticleDO;

import java.util.List;
import java.util.Map;

public interface ColumnService {
    PageListVo<ColumnDTO> listColumn(PageParam pageParam);
    List<SimpleArticleDTO> queryColumnArticles(Long columnId);
    ColumnDTO queryColumnInfo(Long columnId);
    ColumnDTO queryBasicColumnInfo(Long columnId);
    void saveColumn(ColumnDTO columnDTO);
    void deleteColumn(Long columnId);
    ColumnArticleDO getColumnArticleRelation(Long articleId);
    ColumnArticleDO queryColumnArticle(Long columnId, Integer section);
    
    /**
     * 查询用户的专栏列册"
     */
    List<ColumnDTO> queryMyColumns(Long userId);
    List<ColumnDTO> searchColumns(String keyword);
    List<Article> getMyArticles(Long userId);
    void addArticleToColumn(Long columnId, Long articleId);
    void removeArticleFromColumn(Long columnId, Long articleId);
}

