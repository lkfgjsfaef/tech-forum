package com.example.forum.service.user.service;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.user.SearchZsxqUserReq;
import com.example.forum.api.model.vo.user.ZsxqUserBatchOperateReq;
import com.example.forum.api.model.vo.user.ZsxqUserPostReq;
import com.example.forum.api.model.enums.user.UserAIStatEnum;
import com.example.forum.api.model.vo.user.dto.ZsxqUserInfoDTO;

public interface ZsxqWhiteListService {
    PageVo<ZsxqUserInfoDTO> getList(SearchZsxqUserReq req);
    void operate(Long id, UserAIStatEnum status);
    void reset(Integer authorId);
    void batchOperate(java.util.List<Long> ids, UserAIStatEnum status);
    void update(ZsxqUserPostReq req);
}
