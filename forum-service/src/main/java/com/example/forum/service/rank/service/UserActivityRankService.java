package com.example.forum.service.rank.service;

import com.example.forum.api.model.enums.rank.ActivityRankTimeEnum;
import com.example.forum.api.model.vo.rank.dto.RankItemDTO;
import com.example.forum.service.rank.service.model.ActivityScoreBo;

import java.util.List;

public interface UserActivityRankService {
    void addActivityScore(Long userId, String activityType);
    void addActivityScore(Long userId, ActivityScoreBo scoreBo);
    void updateRank(Long userId, int score);
    int getUserRank(Long userId);
    List<RankItemDTO> queryRankList(ActivityRankTimeEnum timeEnum, int limit);
}
