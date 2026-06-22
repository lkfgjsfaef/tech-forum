package com.example.forum.api.model.vo.article;

import lombok.Data;

@Data
public class SearchArticleReq {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String title;
    private Long categoryId;
    private Integer status;
}
