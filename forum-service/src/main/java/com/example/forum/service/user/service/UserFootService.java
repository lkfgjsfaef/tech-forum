package com.example.forum.service.user.service;

import com.example.forum.api.model.enums.DocumentTypeEnum;
import com.example.forum.api.model.enums.OperateTypeEnum;
import com.example.forum.api.model.vo.user.UserRelationReq;
import com.example.forum.api.model.vo.user.dto.FollowUserInfoDTO;

import java.util.List;

public interface UserFootService {
    void collect(Long articleId, Long userId);
    void cancelCollect(Long articleId, Long userId);
    void praise(Long articleId, Long userId);
    void cancelPraise(Long articleId, Long userId);
    void follow(UserRelationReq req, Long userId);
    void cancelFollow(UserRelationReq req, Long userId);
    List<FollowUserInfoDTO> getFollowList(Long userId, Integer page, Integer size);
    void favorArticleComment(DocumentTypeEnum documentType, Long documentId, Long authorUserId, Long loginUserId, OperateTypeEnum operate);
}
