package com.example.forum.service.article.repository.dao;

import com.example.forum.service.article.repository.entity.CategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryDao {
    CategoryDO findById(Long id);
    CategoryDO findByName(@Param("categoryName") String categoryName);
    List<CategoryDO> findAll();
    void insert(CategoryDO category);
    void update(CategoryDO category);
    void deleteById(Long id);
}
