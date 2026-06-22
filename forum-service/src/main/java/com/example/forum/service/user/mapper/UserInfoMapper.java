package com.example.forum.service.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper {
    void insert(@Param("userId") Long userId, @Param("userName") String userName,
                @Param("nickname") String nickname, @Param("photo") String photo,
                @Param("email") String email);

    String queryNickNameByUserId(@Param("userId") Long userId);

    int countByEmail(@Param("email") String email);

    int updateUserInfo(@Param("userId") Long userId, @Param("nickname") String nickname,
                       @Param("photo") String photo, @Param("profile") String profile);
}
