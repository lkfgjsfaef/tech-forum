package com.example.forum.api.model.vo.article;

import lombok.Data;

import java.util.List;

@Data
public class ArticlePostReq {
    private Long id;
    private String title;
    private String shortTitle;
    private String summary;
    private String content;
    private Long categoryId;
    private List<Long> tagIds;
    private String cover;
    private Integer sourceType;
    private Integer source;
    private String sourceUrl;
    
    /**
     * 关联的专栏ID（可选）
     */
    private Long columnId;
}
