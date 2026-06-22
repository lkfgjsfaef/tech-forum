package com.example.forum.api.model.vo.article;

import lombok.Data;

@Data
public class CategoryReq {
    private Long id;
    private String categoryName;
    private Integer rank;
    private Integer status;
}
