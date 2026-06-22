package com.example.forum.service.article.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Article {
    private Long id;
    private String title;
    private String shortTitle;
    private String content;
    private String summary;
    private String cover;
    private Long userId;
    private Long categoryId;
    private String slug;
    private Integer sourceType;
    private String sourceUrl;
    private Integer status;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
