package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.article.dto.ArticlePayInfoDTO;
import com.example.forum.api.model.vo.article.dto.PayConfirmDTO;
import com.example.forum.api.model.vo.user.dto.SimpleUserInfoDTO;
import com.example.forum.service.article.service.ArticlePayService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticlePayServiceImpl implements ArticlePayService {

    @Override
    public ArticlePayInfoDTO queryArticlePayInfo(Long articleId) {
        ArticlePayInfoDTO dto = new ArticlePayInfoDTO();
        dto.setPayed(false);
        dto.setPayCode("");
        dto.setPayAmount(0);
        return dto;
    }

    @Override
    public ArticlePayInfoDTO toPay(Long articleId, Long userId, String notes) {
        ArticlePayInfoDTO dto = new ArticlePayInfoDTO();
        dto.setPayed(false);
        dto.setPayCode("");
        dto.setPayAmount(0);
        return dto;
    }

    @Override
    public boolean payArticle(Long articleId, Long userId) {
        return true;
    }

    @Override
    public boolean checkPayed(Long articleId, Long userId) {
        return true;
    }

    @Override
    public boolean hasPayed(Long articleId, Long userId) {
        return true;
    }

    @Override
    public boolean updatePaying(Long payId, Long userId, String notes) {
        return true;
    }

    @Override
    public boolean updatePayStatus(String payId, String outTradeNo, Integer payStatus, Long successTime, String thirdTransactionId) {
        return true;
    }

    @Override
    public List<SimpleUserInfoDTO> queryPayUsers(Long articleId) {
        return new ArrayList<>();
    }

    @Override
    public PayConfirmDTO buildPayConfirmInfo(Long payId, Long userId) {
        PayConfirmDTO dto = new PayConfirmDTO();
        dto.setPayId(payId);
        dto.setReceiveUserId(userId);
        return dto;
    }
}
