package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    public static final String DEFAULT_TOTAL_CATEGORY = "全部";

    private Long categoryId;
    private String categoryName;
    private String category;
    private Integer articleCount;
    private Boolean selected;

    public CategoryDTO() {
    }

    public CategoryDTO(Long categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
    }
}
