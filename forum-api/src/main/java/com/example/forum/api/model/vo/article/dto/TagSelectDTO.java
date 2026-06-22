package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

@Data
public class TagSelectDTO {
    private Long tagId;
    private String tagName;
    private String selectDesc;
    private Boolean selected;
}
