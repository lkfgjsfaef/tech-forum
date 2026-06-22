package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

@Data
public class ArticlePayInfoDTO {
    private Boolean payed;
    private String payCode;
    private Integer payAmount;
}
