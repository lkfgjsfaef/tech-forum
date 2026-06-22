package com.example.forum.service.pay.service.impl;

import com.example.forum.service.pay.model.PayCallbackBo;
import com.example.forum.service.pay.model.PrePayInfoResBo;
import com.example.forum.service.pay.model.ThirdPayOrderReqBo;
import com.example.forum.service.pay.service.ThirdPayHandler;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.function.Function;

public class DummyThirdPayHandler implements ThirdPayHandler {

    @Override
    public String createPayUrl(Long articleId, Long userId) {
        return null;
    }

    @Override
    public boolean verifyPaySign(Map<String, String> params) {
        return false;
    }

    @Override
    public void handlePayCallback(Map<String, String> params) {
    }

    @Override
    public PrePayInfoResBo createPayOrder(ThirdPayOrderReqBo req) {
        return null;
    }

    @Override
    public ResponseEntity<?> payCallback(HttpServletRequest request, Function<PayCallbackBo, Boolean> callback) {
        return ResponseEntity.ok().build();
    }
}
