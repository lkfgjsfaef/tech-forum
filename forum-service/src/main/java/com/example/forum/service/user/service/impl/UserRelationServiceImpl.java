package com.example.forum.service.user.service.impl;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.user.UserRelationReq;
import com.example.forum.api.model.vo.user.dto.FollowUserInfoDTO;
import com.example.forum.service.user.entity.User;
import com.example.forum.service.user.entity.UserRelationDO;
import com.example.forum.service.user.mapper.UserInfoMapper;
import com.example.forum.service.user.mapper.UserMapper;
import com.example.forum.service.user.mapper.UserRelationMapper;
import com.example.forum.service.user.service.UserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRelationServiceImpl implements UserRelationService {

    @Autowired
    private UserRelationMapper userRelationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void follow(Long userId, Long followUserId) {
        if (userId == null || followUserId == null || userId.equals(followUserId)) {
            return;
        }
        UserRelationDO relation = userRelationMapper.findByUserIdAndFollowUserId(userId, followUserId);
        if (relation == null) {
            relation = new UserRelationDO();
            relation.setUserId(userId);
            relation.setFollowUserId(followUserId);
            relation.setFollowState(1);
            userRelationMapper.insert(relation);
        } else if (relation.getFollowState() != 1) {
            relation.setFollowState(1);
            userRelationMapper.update(relation);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfollow(Long userId, Long followUserId) {
        if (userId == null || followUserId == null) {
            return;
        }
        UserRelationDO relation = userRelationMapper.findByUserIdAndFollowUserId(userId, followUserId);
        if (relation != null && relation.getFollowState() == 1) {
            relation.setFollowState(2);
            userRelationMapper.update(relation);
        }
    }

    @Override
    public PageListVo<FollowUserInfoDTO> queryFollowList(Long userId, PageParam pageParam) {
        int total = userRelationMapper.countFollows(userId);
        if (total == 0) {
            return PageListVo.emptyVo();
        }
        List<UserRelationDO> relations = userRelationMapper.findFollowList(userId, pageParam.getOffset(), pageParam.getPageSize());
        List<FollowUserInfoDTO> list = convertToFollowDTO(relations, userId, true);
        return PageListVo.of(list, (long) total, pageParam.getPageNum(), pageParam.getPageSize());
    }

    @Override
    public PageListVo<FollowUserInfoDTO> queryFansList(Long userId, PageParam pageParam) {
        int total = userRelationMapper.countFans(userId);
        if (total == 0) {
            return PageListVo.emptyVo();
        }
        List<UserRelationDO> relations = userRelationMapper.findFansList(userId, pageParam.getOffset(), pageParam.getPageSize());
        List<FollowUserInfoDTO> list = convertToFollowDTO(relations, userId, false);
        return PageListVo.of(list, (long) total, pageParam.getPageNum(), pageParam.getPageSize());
    }

    @Override
    public boolean isFollowed(Long userId, Long followUserId) {
        if (userId == null || followUserId == null) {
            return false;
        }
        UserRelationDO relation = userRelationMapper.findByUserIdAndFollowUserId(userId, followUserId);
        return relation != null && relation.getFollowState() == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRelation(UserRelationReq req) {
        if (req == null || req.getUserId() == null) {
            return;
        }
        // relationType: 1-关注, 2-取消关注
        if (req.getRelationType() != null && req.getRelationType() == 1) {
            follow(req.getUserId(), req.getFollowUserId());
        } else if (req.getRelationType() != null && req.getRelationType() == 2) {
            unfollow(req.getUserId(), req.getFollowUserId());
        }
    }

    @Override
    public PageListVo<FollowUserInfoDTO> getUserFollowList(Long userId, PageParam pageParam) {
        return queryFollowList(userId, pageParam);
    }

    @Override
    public PageListVo<FollowUserInfoDTO> getUserFansList(Long userId, PageParam pageParam) {
        return queryFansList(userId, pageParam);
    }

    @Override
    public void updateUserFollowRelationId(PageListVo<FollowUserInfoDTO> followList, Long userId) {
        if (followList == null || followList.getList() == null || userId == null) {
            return;
        }
        for (FollowUserInfoDTO dto : followList.getList()) {
            dto.setFollowed(isFollowed(userId, dto.getUserId()));
        }
    }

    private List<FollowUserInfoDTO> convertToFollowDTO(List<UserRelationDO> relations, Long currentUserId, boolean isFollowList) {
        List<FollowUserInfoDTO> result = new ArrayList<>();
        for (UserRelationDO relation : relations) {
            Long targetUserId = isFollowList ? relation.getFollowUserId() : relation.getUserId();
            User user = userMapper.findById(targetUserId);
            if (user == null) {
                continue;
            }
            FollowUserInfoDTO dto = new FollowUserInfoDTO();
            dto.setUserId(user.getId());
            // 从 user_info 读取昵称作为显示名
            String nickName = userInfoMapper.queryNickNameByUserId(user.getId());
            String displayName = (nickName != null && !nickName.isEmpty()) ? nickName : user.getUsername();
            dto.setUserName(displayName);
            dto.setNickName(nickName);
            dto.setPhoto(user.getAvatar());
            dto.setFollowed(isFollowList || isFollowed(currentUserId, targetUserId));
            result.add(dto);
        }
        return result;
    }
}

