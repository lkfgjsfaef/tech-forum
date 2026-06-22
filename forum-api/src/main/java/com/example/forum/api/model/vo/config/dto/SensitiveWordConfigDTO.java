package com.example.forum.api.model.vo.config.dto;

import lombok.Data;

@Data
public class SensitiveWordConfigDTO {
    private Boolean openSensitiveWord;
    private String sensitiveWord;
}
