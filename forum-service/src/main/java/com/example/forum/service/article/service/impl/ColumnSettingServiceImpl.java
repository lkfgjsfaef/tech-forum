package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.article.*;
import com.example.forum.api.model.vo.article.dto.*;
import com.example.forum.service.article.service.ColumnSettingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColumnSettingServiceImpl implements ColumnSettingService {

    @Override
    public void saveColumn(ColumnReq req) {
    }

    @Override
    public void saveColumnArticleGroup(ColumnArticleGroupReq req) {
    }

    @Override
    public void saveColumnArticle(ColumnArticleReq req) {
    }

    @Override
    public void deleteColumn(Long columnId) {
    }

    @Override
    public boolean deleteColumnGroup(Long groupId) {
        return true;
    }

    @Override
    public void deleteColumnArticle(Long id) {
    }

    @Override
    public void sortColumnArticleApi(SortColumnArticleReq req) {
    }

    @Override
    public void sortColumnArticleByIDApi(SortColumnArticleByIDReq req) {
    }

    @Override
    public void moveColumnArticleOrGroup(MoveColumnArticleOrGroupReq req) {
    }

    @Override
    public PageVo<ColumnDTO> getColumnList(SearchColumnReq req) {
        PageVo<ColumnDTO> result = new PageVo<>();
        result.setList(new ArrayList<>());
        result.setTotal(0L);
        return result;
    }

    @Override
    public List<ColumnArticleGroupDTO> getColumnGroups(Long columnId) {
        return new ArrayList<>();
    }

    @Override
    public PageVo<ColumnArticleDTO> getColumnArticleList(SearchColumnArticleReq req) {
        PageVo<ColumnArticleDTO> result = new PageVo<>();
        result.setList(new ArrayList<>());
        result.setTotal(0L);
        return result;
    }

    @Override
    public List<ColumnArticleGroupDTO> getColumnGroupAndArticles(Long columnId) {
        return new ArrayList<>();
    }

    @Override
    public List<SimpleColumnDTO> listSimpleColumnBySearchKey(String key) {
        return new ArrayList<>();
    }
}
