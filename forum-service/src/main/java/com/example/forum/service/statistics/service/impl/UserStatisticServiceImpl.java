package com.example.forum.service.statistics.service.impl;

import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import com.example.forum.service.statistics.service.UserStatisticService;
import org.springframework.stereotype.Service;

@Service
public class UserStatisticServiceImpl implements UserStatisticService {

    @Override
    public void incrArticleReadCount(Long articleId) {
    }

    @Override
    public void incrArticleLikeCount(Long articleId) {
    }

    @Override
    public void incrUserReadCount(Long userId) {
    }

    @Override
    public void incrUserLikeCount(Long userId) {
    }

    @Override
    public BaseUserInfoDTO getUserStatistic(Long userId) {
        return null;
    }

    @Override
    public int getOnlineUserCnt() {
        return 0;
    }

    @Override
    public void incrOnlineUserCnt(int delta) {
    }
}
