package com.example.forum.service.agent.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("agent_session_tag_relation")
public class SessionTagRelation {
    @TableId
    private Long sessionId;
    private Long tagId;
}
