package com.example.forum.service.rank.service.impl;

import com.example.forum.api.model.enums.rank.ActivityRankTimeEnum;
import com.example.forum.api.model.vo.rank.dto.RankItemDTO;
import com.example.forum.service.rank.repository.dao.RankDao;
import com.example.forum.service.rank.service.UserActivityRankService;
import com.example.forum.service.rank.service.model.ActivityScoreBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserActivityRankServiceImpl implements UserActivityRankService {

    @Autowired
    private RankDao rankDao;

    @Override
    public void addActivityScore(Long userId, String activityType) {
    }

    @Override
    @CacheEvict(value = "rankList", allEntries = true)
    public void addActivityScore(Long userId, ActivityScoreBo scoreBo) {
    }

    @Override
    @CacheEvict(value = "rankList", allEntries = true)
    public void updateRank(Long userId, int score) {
    }

    @Override
    public int getUserRank(Long userId) {
        return 0;
    }

    @Override
    @Cacheable(value = "rankList", key = "#timeEnum.name() + ':' + #limit")
    public List<RankItemDTO> queryRankList(ActivityRankTimeEnum timeEnum, int limit) {
        int days;
        switch (timeEnum) {
            case DAY: days = 1; break;
            case WEEK: days = 7; break;
            case MONTH: days = 30; break;
            default: days = 0; break;
        }
        List<RankItemDTO> list = rankDao.queryActivityRank(days, limit);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i + 1);
        }
        return list;
    }
}
