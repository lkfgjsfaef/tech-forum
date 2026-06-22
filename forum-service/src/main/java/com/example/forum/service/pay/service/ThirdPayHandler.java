package com.example.forum.service.pay.service;

import com.example.forum.service.pay.model.PayCallbackBo;
import com.example.forum.service.pay.model.PrePayInfoResBo;
import com.example.forum.service.pay.model.ThirdPayOrderReqBo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.function.Function;

public interface ThirdPayHandler {
    String createPayUrl(Long articleId, Long userId);
    boolean verifyPaySign(Map<String, String> params);
    void handlePayCallback(Map<String, String> params);
    PrePayInfoResBo createPayOrder(ThirdPayOrderReqBo req);
    ResponseEntity<?> payCallback(HttpServletRequest request, Function<PayCallbackBo, Boolean> callback);
}
