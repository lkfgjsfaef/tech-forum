package com.example.forum.api.model.vo.config;

import lombok.Data;

@Data
public class GlobalConfigReq {
    private String configKey;
    private String configValue;
    private Integer type;
}
