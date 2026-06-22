package com.example.forum.service.rank.repository.dao;

import com.example.forum.api.model.vo.rank.dto.RankItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RankDao {
    List<RankItemDTO> queryActivityRank(@Param("days") int days, @Param("limit") int limit);
}
