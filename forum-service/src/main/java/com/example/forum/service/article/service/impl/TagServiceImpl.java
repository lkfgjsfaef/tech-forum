package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.article.dto.TagDTO;
import com.example.forum.service.article.repository.dao.TagDao;
import com.example.forum.service.article.repository.entity.TagDO;
import com.example.forum.service.article.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    @Cacheable(value = "tags", key = "'all'")
    public List<TagDTO> loadAllTags() {
        List<TagDO> tags = tagDao.findAll();
        return tags.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public TagDTO queryTag(Long tagId) {
        TagDO tag = tagDao.findById(tagId);
        return tag != null ? toDTO(tag) : null;
    }

    @Override
    @CacheEvict(value = "tags", key = "'all'")
    public Long saveTag(TagDTO tagDTO) {
        TagDO tag = new TagDO();
        tag.setTagName(tagDTO.getTag());
        tag.setStatus(1);
        tagDao.insert(tag);
        return tag.getId();
    }

    @Override
    @CacheEvict(value = "tags", key = "'all'")
    public void updateTag(TagDTO tagDTO) {
        TagDO tag = tagDao.findById(tagDTO.getTagId());
        if (tag != null) {
            tag.setTagName(tagDTO.getTag());
            tagDao.update(tag);
        }
    }

    @Override
    @CacheEvict(value = "tags", key = "'all'")
    public void deleteTag(Long tagId) {
        tagDao.deleteById(tagId);
    }

    @Override
    public List<TagDTO> queryTagsByArticleId(Long articleId) {
        return new ArrayList<>();
    }

    @Override
    public PageVo<TagDTO> queryTags(String key, PageParam pageParam) {
        List<TagDO> all = tagDao.findAll();
        List<TagDTO> filtered = all.stream()
                .filter(t -> key == null || key.isEmpty() || t.getTagName().contains(key))
                .map(this::toDTO)
                .collect(Collectors.toList());

        PageVo<TagDTO> result = new PageVo<>();
        int total = filtered.size();
        int start = (int) ((pageParam.getPageNum() - 1) * pageParam.getPageSize());
        int end = Math.min(start + (int) pageParam.getPageSize(), total);
        if (start < total) {
            result.setList(filtered.subList(start, end));
        } else {
            result.setList(new ArrayList<>());
        }
        result.setTotal((long) total);
        return result;
    }

    @Override
    public Long queryTagId(String tagName) {
        List<TagDTO> all = loadAllTags();
        for (TagDTO t : all) {
            if (t.getTag().equals(tagName)) {
                return t.getTagId();
            }
        }
        return null;
    }

    private TagDTO toDTO(TagDO tag) {
        TagDTO dto = new TagDTO();
        dto.setTagId(tag.getId());
        dto.setTag(tag.getTagName());
        dto.setCategory(tag.getTagType() != null ? tag.getTagType() : 0);
        dto.setStatus(tag.getStatus());
        if (tag.getArticleCount() != null) {
            dto.setCount(tag.getArticleCount());
        }
        return dto;
    }
}
