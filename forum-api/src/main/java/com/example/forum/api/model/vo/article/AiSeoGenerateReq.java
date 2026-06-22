package com.example.forum.api.model.vo.article;

import lombok.Data;

@Data
public class AiSeoGenerateReq {
    private Long articleId;
    private String title;
    private String content;
}
