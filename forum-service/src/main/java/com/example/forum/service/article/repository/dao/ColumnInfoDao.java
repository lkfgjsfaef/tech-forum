package com.example.forum.service.article.repository.dao;

import com.example.forum.service.article.repository.entity.ColumnInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ColumnInfoDao {
    List<ColumnInfoDO> findAllPublished(@Param("offset") long offset, @Param("pageSize") int pageSize);

    int countAllPublished();

    ColumnInfoDO findById(@Param("id") Long id);

    List<ColumnInfoDO> findByUserId(@Param("userId") Long userId);

    void insert(ColumnInfoDO columnInfo);

    void update(ColumnInfoDO columnInfo);

    void deleteById(@Param("id") Long id);

    List<ColumnInfoDO> searchByKeyword(@Param("keyword") String keyword);
}
