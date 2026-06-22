package com.example.forum.service.user.service.impl;

import com.example.forum.api.model.enums.user.UserAIStatEnum;
import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.user.SearchZsxqUserReq;
import com.example.forum.api.model.vo.user.ZsxqUserPostReq;
import com.example.forum.api.model.vo.user.dto.ZsxqUserInfoDTO;
import com.example.forum.service.user.service.ZsxqWhiteListService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ZsxqWhiteListServiceImpl implements ZsxqWhiteListService {

    @Override
    public PageVo<ZsxqUserInfoDTO> getList(SearchZsxqUserReq req) {
        PageVo<ZsxqUserInfoDTO> result = new PageVo<>();
        result.setList(new ArrayList<>());
        result.setTotal(0L);
        return result;
    }

    @Override
    public void operate(Long id, UserAIStatEnum status) {
    }

    @Override
    public void reset(Integer authorId) {
    }

    @Override
    public void batchOperate(List<Long> ids, UserAIStatEnum status) {
    }

    @Override
    public void update(ZsxqUserPostReq req) {
    }
}
