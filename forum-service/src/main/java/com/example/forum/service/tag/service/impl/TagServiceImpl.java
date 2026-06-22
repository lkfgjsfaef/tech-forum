package com.example.forum.service.tag.service.impl;

import com.example.forum.service.tag.entity.Tag;
import com.example.forum.service.tag.mapper.TagMapper;
import com.example.forum.service.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("tagServiceExt")
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public void create(Tag tag) {
        tag.setCount(0);
        tag.setCreateTime(LocalDateTime.now());
        tag.setUpdateTime(LocalDateTime.now());
        tagMapper.insert(tag);
    }

    @Override
    public void update(Tag tag) {
        tag.setUpdateTime(LocalDateTime.now());
        tagMapper.update(tag);
    }

    @Override
    public Tag findById(Long id) {
        return tagMapper.findById(id);
    }

    @Override
    public Tag findByName(String name) {
        return tagMapper.findByName(name);
    }

    @Override
    public List<Tag> findAll() {
        return tagMapper.findAll();
    }

    @Override
    public void incrementCount(Long id) {
        tagMapper.incrementCount(id);
    }
}
