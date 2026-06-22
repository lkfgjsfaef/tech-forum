package com.example.forum.service.statistics.service;

import com.example.forum.api.model.vo.statistics.dto.StatisticsCountDTO;
import com.example.forum.api.model.vo.statistics.dto.StatisticsDayDTO;
import com.example.forum.service.sitemap.model.SiteCntVo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface StatisticsSettingService {
    void incrVisitCount(String path);
    Long getVisitCount(String path);
    void saveRequestCount(String clientIp);
    SiteCntVo getStatisticsCount();
    StatisticsCountDTO getStatisticsCountDTO();
    List<StatisticsDayDTO> getPvUvDayList(Integer day);
    void download2Excel(Integer day, HttpServletResponse response);
}
