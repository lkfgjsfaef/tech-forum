package com.example.forum.api.model.vo.user;

import lombok.Data;

@Data
public class SearchZsxqUserReq {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String key;
}
