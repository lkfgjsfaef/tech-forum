package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.article.dto.CategoryDTO;
import com.example.forum.service.article.repository.dao.CategoryDao;
import com.example.forum.service.article.repository.entity.CategoryDO;
import com.example.forum.service.article.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    @Cacheable(value = "categories", key = "'all'")
    public List<CategoryDTO> loadAllCategories() {
        List<CategoryDO> list = categoryDao.findAll();
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 按categoryName去重，保留ID最大的记录（最新的册"
        return list.stream()
                .collect(Collectors.toMap(
                    CategoryDO::getCategoryName,
                    category -> category,
                    (existing, replacement) -> existing.getId() > replacement.getId() ? existing : replacement
                ))
                .values()
                .stream()
                .sorted((a, b) -> {
                    // 先按rank排序，再按id排序
                    int rankCompare = Integer.compare(a.getRank(), b.getRank());
                    return rankCompare != 0 ? rankCompare : Long.compare(a.getId(), b.getId());
                })
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO queryCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        CategoryDO category = categoryDao.findById(categoryId);
        return toDTO(category);
    }

    @Override
    @CacheEvict(value = "categories", key = "'all'")
    public Long saveCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return 0L;
        }
        CategoryDO entity = new CategoryDO();
        entity.setCategoryName(categoryDTO.getCategory());
        entity.setStatus(1);
        entity.setRank(0);
        categoryDao.insert(entity);
        return entity.getId();
    }

    @Override
    @CacheEvict(value = "categories", key = "'all'")
    public void updateCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null || categoryDTO.getCategoryId() == null) {
            return;
        }
        CategoryDO entity = new CategoryDO();
        entity.setId(categoryDTO.getCategoryId());
        entity.setCategoryName(categoryDTO.getCategory());
        entity.setStatus(1);
        categoryDao.update(entity);
    }

    @Override
    @CacheEvict(value = "categories", key = "'all'")
    public void deleteCategory(Long categoryId) {
        if (categoryId != null) {
            categoryDao.deleteById(categoryId);
        }
    }

    @Override
    public Long queryCategoryId(String categoryName) {
        if (categoryName == null) {
            return null;
        }
        List<CategoryDTO> all = loadAllCategories();
        for (CategoryDTO c : all) {
            if (categoryName.equals(c.getCategory())) {
                return c.getCategoryId();
            }
        }
        return null;
    }

    private CategoryDTO toDTO(CategoryDO entity) {
        if (entity == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setCategoryName(entity.getCategoryName());
        return dto;
    }
}

