package com.example.forum.service.pay.model;

import lombok.Data;

@Data
public class PayCallbackBo {
    private String payId;
    private String outTradeNo;
    private Long articleId;
    private Long userId;
    private Integer payStatus;
    private Long successTime;
    private String thirdTransactionId;
    private String sign;
}
