package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.article.dto.ArticlePayInfoDTO;
import com.example.forum.api.model.vo.article.dto.PayConfirmDTO;
import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import com.example.forum.api.model.vo.user.dto.SimpleUserInfoDTO;

import java.util.List;

public interface ArticlePayService {
    ArticlePayInfoDTO queryArticlePayInfo(Long articleId);
    ArticlePayInfoDTO toPay(Long articleId, Long userId, String notes);
    boolean payArticle(Long articleId, Long userId);
    boolean checkPayed(Long articleId, Long userId);
    boolean hasPayed(Long articleId, Long userId);
    boolean updatePaying(Long payId, Long userId, String notes);
    boolean updatePayStatus(String payId, String outTradeNo, Integer payStatus, Long successTime, String thirdTransactionId);
    List<SimpleUserInfoDTO> queryPayUsers(Long articleId);
    PayConfirmDTO buildPayConfirmInfo(Long payId, Long userId);
}
