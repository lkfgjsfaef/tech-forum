package com.example.forum.api.model.vo.article;

import lombok.Data;

@Data
public class ContentPostReq {
    private Long articleId;
    private String content;
}
