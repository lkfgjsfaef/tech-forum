package com.example.forum.service.sitemap.service;

import com.example.forum.service.sitemap.model.SiteCntVo;
import com.example.forum.service.sitemap.model.SiteMapVo;

import java.time.LocalDate;

public interface SitemapService {
    String getSitemap();
    SiteCntVo getSiteCnt();
    SiteCntVo querySiteVisitInfo(LocalDate date, String path);
    void saveVisitInfo(String clientIp, String path);
    SiteMapVo getSiteMap();
    String getRobotsTxt();
    void refreshSitemap();
}
