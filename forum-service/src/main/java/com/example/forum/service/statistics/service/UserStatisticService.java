package com.example.forum.service.statistics.service;

import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;

public interface UserStatisticService {

    void incrArticleReadCount(Long articleId);

    void incrArticleLikeCount(Long articleId);

    void incrUserReadCount(Long userId);

    void incrUserLikeCount(Long userId);

    BaseUserInfoDTO getUserStatistic(Long userId);

    int getOnlineUserCnt();

    void incrOnlineUserCnt(int delta);
}
