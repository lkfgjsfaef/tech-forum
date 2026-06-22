package com.example.forum.api.model.vo.config.dto;

import lombok.Data;

@Data
public class GlobalConfigDTO {
    private Long id;
    private String configKey;
    private String configValue;
    private Integer type;
}
