package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.article.SearchTagReq;
import com.example.forum.api.model.vo.article.TagReq;
import com.example.forum.api.model.vo.article.dto.TagDTO;
import com.example.forum.service.article.service.TagSettingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagSettingServiceImpl implements TagSettingService {

    @Override
    public List<TagDTO> loadAllTags() {
        return new ArrayList<>();
    }

    @Override
    public Long saveTag(TagDTO tagDTO) {
        return 0L;
    }

    @Override
    public Long saveTag(TagReq req) {
        return 0L;
    }

    @Override
    public void updateTag(TagDTO tagDTO) {
    }

    @Override
    public void deleteTag(Long tagId) {
    }

    @Override
    public void deleteTag(Integer tagId) {
        if (tagId != null) {
            deleteTag(Long.valueOf(tagId));
        }
    }

    @Override
    public void operateTag(Integer tagId, Integer pushStatus) {
        if (tagId != null) {
            operateTag(Long.valueOf(tagId), pushStatus);
        }
    }

    @Override
    public void operateTag(Long tagId, Integer pushStatus) {
    }

    @Override
    public PageVo<TagDTO> getTagList(SearchTagReq req) {
        PageVo<TagDTO> result = new PageVo<>();
        result.setList(new ArrayList<>());
        result.setTotal(0L);
        return result;
    }
}
