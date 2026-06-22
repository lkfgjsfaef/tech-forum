package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.article.dto.TagDTO;

import java.util.List;

public interface TagService {
    List<TagDTO> loadAllTags();
    TagDTO queryTag(Long tagId);
    Long saveTag(TagDTO tagDTO);
    void updateTag(TagDTO tagDTO);
    void deleteTag(Long tagId);
    List<TagDTO> queryTagsByArticleId(Long articleId);
    PageVo<TagDTO> queryTags(String key, PageParam pageParam);
    Long queryTagId(String tagName);
}
