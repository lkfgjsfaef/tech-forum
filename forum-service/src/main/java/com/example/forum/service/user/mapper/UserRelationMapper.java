package com.example.forum.service.user.mapper;

import com.example.forum.service.user.entity.UserRelationDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRelationMapper {
    void insert(UserRelationDO userRelation);

    void update(UserRelationDO userRelation);

    UserRelationDO findByUserIdAndFollowUserId(@Param("userId") Long userId,
                                                @Param("followUserId") Long followUserId);

    /**
     * 查询用户的关注列册"
     */
    List<UserRelationDO> findFollowList(@Param("userId") Long userId,
                                         @Param("offset") long offset,
                                         @Param("limit") int limit);

    /**
     * 查询用户的粉丝列册"
     */
    List<UserRelationDO> findFansList(@Param("userId") Long userId,
                                       @Param("offset") long offset,
                                       @Param("limit") int limit);

    /**
     * 统计用户的关注数
     */
    int countFollows(@Param("userId") Long userId);

    /**
     * 统计用户的粉丝数
     */
    int countFans(@Param("userId") Long userId);
}

