package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

@Data
public class TagDTO {
    private Long tagId;
    private String tagName;
    private String tag;
    private Integer category;
    private Integer status;
    private Integer articleCount;
    private Integer count;

    public String getTag() {
        return this.tag;
    }
}
