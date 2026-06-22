package com.example.forum.service.statistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.forum.service.statistics.repository.entity.RequestCountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * 请求计数Mapper
 */
@Mapper
public interface RequestCountMapper extends BaseMapper<RequestCountDO> {
    
    RequestCountDO selectByDate(@Param("date") LocalDate date);
}
