package com.example.forum.service.user.mapper;

import com.example.forum.service.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    User findByEmail(@Param("email") String email);
    void insert(User user);
    void update(User user);
    User findById(@Param("id") Long id);
    User findBySession(@Param("session") String session);
    List<User> searchUser(@Param("key") String key);

    // 统计用户总数（用于站点统计）
    long countAll();
}
