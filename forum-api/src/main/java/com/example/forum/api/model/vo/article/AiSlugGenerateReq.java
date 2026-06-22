package com.example.forum.api.model.vo.article;

import lombok.Data;

@Data
public class AiSlugGenerateReq {
    private Long articleId;
    private String title;
}
