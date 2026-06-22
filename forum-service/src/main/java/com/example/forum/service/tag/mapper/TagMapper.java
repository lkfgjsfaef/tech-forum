package com.example.forum.service.tag.mapper;

import com.example.forum.service.tag.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {
    void insert(Tag tag);
    void update(Tag tag);
    Tag findById(@Param("id") Long id);
    Tag findByName(@Param("name") String name);
    List<Tag> findAll();
    void incrementCount(@Param("id") Long id);
}
