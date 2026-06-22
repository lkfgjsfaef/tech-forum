package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

@Data
public class ColumnArticleDTO {
    private Long id;
    private Long columnId;
    private Long articleId;
    private String articleTitle;
    private Long groupId;
    private String groupName;
    private Integer sort;
}
