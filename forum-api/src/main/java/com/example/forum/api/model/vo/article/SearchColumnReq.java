package com.example.forum.api.model.vo.article;

import lombok.Data;

@Data
public class SearchColumnReq {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String key;
}
