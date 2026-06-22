package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.article.ColumnArticleGroupReq;
import com.example.forum.api.model.vo.article.ColumnArticleReq;
import com.example.forum.api.model.vo.article.ColumnReq;
import com.example.forum.api.model.vo.article.MoveColumnArticleOrGroupReq;
import com.example.forum.api.model.vo.article.SearchColumnArticleReq;
import com.example.forum.api.model.vo.article.SearchColumnReq;
import com.example.forum.api.model.vo.article.SortColumnArticleByIDReq;
import com.example.forum.api.model.vo.article.SortColumnArticleReq;
import com.example.forum.api.model.vo.article.dto.ColumnArticleDTO;
import com.example.forum.api.model.vo.article.dto.ColumnArticleGroupDTO;
import com.example.forum.api.model.vo.article.dto.ColumnDTO;
import com.example.forum.api.model.vo.article.dto.SimpleColumnDTO;

import java.util.List;

public interface ColumnSettingService {
    void saveColumn(ColumnReq req);
    void saveColumnArticleGroup(ColumnArticleGroupReq req);
    void saveColumnArticle(ColumnArticleReq req);
    void deleteColumn(Long columnId);
    boolean deleteColumnGroup(Long groupId);
    void deleteColumnArticle(Long id);
    void sortColumnArticleApi(SortColumnArticleReq req);
    void sortColumnArticleByIDApi(SortColumnArticleByIDReq req);
    void moveColumnArticleOrGroup(MoveColumnArticleOrGroupReq req);
    PageVo<ColumnDTO> getColumnList(SearchColumnReq req);
    List<ColumnArticleGroupDTO> getColumnGroups(Long columnId);
    PageVo<ColumnArticleDTO> getColumnArticleList(SearchColumnArticleReq req);
    List<ColumnArticleGroupDTO> getColumnGroupAndArticles(Long columnId);
    List<SimpleColumnDTO> listSimpleColumnBySearchKey(String key);
}
