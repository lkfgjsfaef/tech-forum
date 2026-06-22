package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.article.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> loadAllCategories();
    CategoryDTO queryCategory(Long categoryId);
    Long saveCategory(CategoryDTO categoryDTO);
    void updateCategory(CategoryDTO categoryDTO);
    void deleteCategory(Long categoryId);
    Long queryCategoryId(String categoryName);
}
