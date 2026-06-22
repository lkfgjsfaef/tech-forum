package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticleAdminDTO {
    private Long id;
    private String title;
    private String shortTitle;
    private String summary;
    private Long categoryId;
    private String categoryName;
    private List<Long> tagIds;
    private String cover;
    private Integer status;
    private Long createTime;
    private Long updateTime;
}
