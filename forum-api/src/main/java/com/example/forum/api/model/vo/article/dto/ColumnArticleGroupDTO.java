package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

import java.util.List;

@Data
public class ColumnArticleGroupDTO {
    private Long id;
    private String groupName;
    private Integer sort;
    private List<ColumnArticleDTO> articles;
}
