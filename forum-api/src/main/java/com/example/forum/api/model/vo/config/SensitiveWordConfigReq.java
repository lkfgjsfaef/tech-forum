package com.example.forum.api.model.vo.config;

import lombok.Data;

@Data
public class SensitiveWordConfigReq {
    private Boolean openSensitiveWord;
    private String sensitiveWord;
}
