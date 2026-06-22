package com.example.forum.service.article.repository.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticleDO {
    private Long id;
    private String title;
    private String shortTitle;
    private String summary;
    private String content;
    private String cover;
    private Long authorId;
    private Long userId;
    private Long categoryId;
    private String slug;
    private Integer status;
    private Integer payStatus;
    private Integer readType;
    private Integer readCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer sourceType;
    private String sourceUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public String getUrlSlug() {
        return this.slug;
    }
}
