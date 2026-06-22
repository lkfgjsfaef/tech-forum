package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.article.SearchTagReq;
import com.example.forum.api.model.vo.article.TagReq;
import com.example.forum.api.model.vo.article.dto.TagDTO;

import java.util.List;

public interface TagSettingService {
    List<TagDTO> loadAllTags();
    Long saveTag(TagDTO tagDTO);
    Long saveTag(TagReq req);
    void updateTag(TagDTO tagDTO);
    void deleteTag(Long tagId);
    void deleteTag(Integer tagId);
    void operateTag(Integer tagId, Integer pushStatus);
    void operateTag(Long tagId, Integer pushStatus);
    PageVo<TagDTO> getTagList(SearchTagReq req);
}
