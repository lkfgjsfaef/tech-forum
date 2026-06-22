package com.example.forum.service.statistics.service.impl;

import com.example.forum.api.model.vo.statistics.dto.StatisticsCountDTO;
import com.example.forum.api.model.vo.statistics.dto.StatisticsDayDTO;
import com.example.forum.service.sitemap.model.SiteCntVo;
import com.example.forum.service.statistics.service.StatisticsSettingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsSettingServiceImpl implements StatisticsSettingService {

    @Override
    public void incrVisitCount(String path) {
    }

    @Override
    public Long getVisitCount(String path) {
        return 0L;
    }

    @Override
    public void saveRequestCount(String clientIp) {
    }

    @Override
    public SiteCntVo getStatisticsCount() {
        SiteCntVo vo = new SiteCntVo();
        vo.setPvCount(0L);
        vo.setUvCount(0L);
        return vo;
    }

    @Override
    public StatisticsCountDTO getStatisticsCountDTO() {
        StatisticsCountDTO dto = new StatisticsCountDTO();
        dto.setPv(0L);
        dto.setUv(0L);
        dto.setArticleCount(0L);
        dto.setUserCount(0L);
        return dto;
    }

    @Override
    public List<StatisticsDayDTO> getPvUvDayList(Integer day) {
        return new ArrayList<>();
    }

    @Override
    public void download2Excel(Integer day, HttpServletResponse response) {
    }
}
