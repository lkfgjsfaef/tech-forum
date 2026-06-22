package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

@Data
public class ColumnDTO {
    private Long columnId;
    private String columnName;
    private String column;
    private String description;
    private String introduction;
    private String cover;
    private String authorName;
    private Long author;
    private Integer state;
    private Integer articleCount;
    private Integer nums;
    private Integer type;
    private Long publishTime;
    private Long freeStartTime;
    private Long freeEndTime;
    private java.util.List<SimpleArticleDTO> articles;
}
