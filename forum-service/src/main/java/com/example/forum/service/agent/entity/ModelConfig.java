package com.example.forum.service.agent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("agent_model_config")
public class ModelConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String modelName;

    private String apiKey;

    private String endpoint;

    private Integer status;

    private LocalDateTime createTime;
}
