package com.example.forum.service.user.service;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.user.UserRelationReq;
import com.example.forum.api.model.vo.user.dto.FollowUserInfoDTO;

public interface UserRelationService {
    void follow(Long userId, Long followUserId);
    void unfollow(Long userId, Long followUserId);
    PageListVo<FollowUserInfoDTO> queryFollowList(Long userId, PageParam pageParam);
    PageListVo<FollowUserInfoDTO> queryFansList(Long userId, PageParam pageParam);
    boolean isFollowed(Long userId, Long followUserId);
    void saveUserRelation(UserRelationReq req);
    PageListVo<FollowUserInfoDTO> getUserFollowList(Long userId, PageParam pageParam);
    PageListVo<FollowUserInfoDTO> getUserFansList(Long userId, PageParam pageParam);
    void updateUserFollowRelationId(PageListVo<FollowUserInfoDTO> followList, Long userId);
}
