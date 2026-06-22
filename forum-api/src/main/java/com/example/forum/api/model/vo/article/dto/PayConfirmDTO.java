package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

@Data
public class PayConfirmDTO {
    private Long articleId;
    private Long payId;
    private Long receiveUserId;
    private String title;
    private String payUrl;
    private String qrCode;
}
