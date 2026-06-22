package com.niit.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niit.agent.entity.SessionTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SessionTagMapper extends BaseMapper<SessionTag> {

    @Select("SELECT t.* FROM session_tag t JOIN session_tag_relation r ON t.id = r.tag_id WHERE r.session_id = #{sessionId}")
    List<SessionTag> getTagsBySessionId(@Param("sessionId") Long sessionId);
}
