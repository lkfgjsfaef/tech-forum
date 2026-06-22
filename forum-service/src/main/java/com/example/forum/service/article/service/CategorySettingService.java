package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.article.CategoryReq;
import com.example.forum.api.model.vo.article.SearchCategoryReq;
import com.example.forum.api.model.vo.article.dto.CategoryDTO;

public interface CategorySettingService {
    PageVo<CategoryDTO> getCategoryList(SearchCategoryReq req);
    CategoryDTO getCategoryDetail(Long categoryId);
    void saveCategory(CategoryReq req);
    void deleteCategory(Long categoryId);
    void deleteCategory(Integer categoryId);
    void operateCategory(Integer categoryId, Integer pushStatus);
    void operateCategory(Long categoryId, Integer pushStatus);
}
