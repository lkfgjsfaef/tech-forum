package com.example.forum.api.model.vo.config;

import lombok.Data;

@Data
public class SearchSensitiveWordHitReq {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
