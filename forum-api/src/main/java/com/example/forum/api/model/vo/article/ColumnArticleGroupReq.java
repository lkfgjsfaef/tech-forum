package com.example.forum.api.model.vo.article;

import lombok.Data;

@Data
public class ColumnArticleGroupReq {
    private Long id;
    private Long columnId;
    private String groupName;
    private Integer sort;
}
