package com.example.forum.api.model.vo.config;

import lombok.Data;

@Data
public class SearchGlobalConfigReq {
    private String key;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
