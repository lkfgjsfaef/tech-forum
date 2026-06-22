package com.example.forum.service.article.repository.dao;

import com.example.forum.service.article.repository.entity.TagDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagDao {
    TagDO findById(Long id);
    List<TagDO> findAll();
    void insert(TagDO tag);
    void update(TagDO tag);
    void deleteById(Long id);
}
