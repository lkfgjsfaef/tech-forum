package com.example.forum.api.model.vo.article;

import lombok.Data;

@Data
public class ColumnReq {
    private Long id;
    private String columnName;
    private String cover;
    private String summary;
    private Integer sort;
}
