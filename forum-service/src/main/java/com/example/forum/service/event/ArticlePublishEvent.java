package com.example.forum.service.event;

import lombok.Data;

@Data
public class ArticlePublishEvent {
    private Long articleId;
    private String title;
    private Long authorId;
    private String authorName;
    private Long categoryId;
}
