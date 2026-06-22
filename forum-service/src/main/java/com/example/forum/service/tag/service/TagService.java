package com.example.forum.service.tag.service;

import com.example.forum.service.tag.entity.Tag;

import java.util.List;

public interface TagService {
    void create(Tag tag);
    void update(Tag tag);
    Tag findById(Long id);
    Tag findByName(String name);
    List<Tag> findAll();
    void incrementCount(Long id);
}
