package com.example.forum.api.model.vo.config.dto;

import lombok.Data;

@Data
public class SensitiveWordHitDTO {
    private String word;
    private Integer hitCount;
}
