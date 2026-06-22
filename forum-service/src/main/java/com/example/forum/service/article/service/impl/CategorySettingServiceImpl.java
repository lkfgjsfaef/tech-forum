package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.article.CategoryReq;
import com.example.forum.api.model.vo.article.SearchCategoryReq;
import com.example.forum.api.model.vo.article.dto.CategoryDTO;
import com.example.forum.service.article.service.CategorySettingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategorySettingServiceImpl implements CategorySettingService {

    @Override
    public PageVo<CategoryDTO> getCategoryList(SearchCategoryReq req) {
        PageVo<CategoryDTO> result = new PageVo<>();
        result.setList(new ArrayList<>());
        result.setTotal(0L);
        return result;
    }

    @Override
    public CategoryDTO getCategoryDetail(Long categoryId) {
        return null;
    }

    @Override
    public void saveCategory(CategoryReq req) {
    }

    @Override
    public void deleteCategory(Long categoryId) {
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        if (categoryId != null) {
            deleteCategory(Long.valueOf(categoryId));
        }
    }

    @Override
    public void operateCategory(Integer categoryId, Integer pushStatus) {
        if (categoryId != null) {
            operateCategory(Long.valueOf(categoryId), pushStatus);
        }
    }

    @Override
    public void operateCategory(Long categoryId, Integer pushStatus) {
    }
}
