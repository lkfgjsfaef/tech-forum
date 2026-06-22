package com.example.forum.service.sitemap.service.impl;

import com.example.forum.service.comment.mapper.CommentMapper;
import com.example.forum.service.sitemap.model.SiteCntVo;
import com.example.forum.service.sitemap.model.SiteMapVo;
import com.example.forum.service.sitemap.service.SitemapService;
import com.example.forum.service.statistics.repository.entity.RequestCountDO;
import com.example.forum.service.statistics.mapper.RequestCountMapper;
import com.example.forum.service.user.mapper.UserMapper;
import com.example.forum.service.article.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SitemapServiceImpl implements SitemapService {
    
    @Autowired
    private ArticleMapper articleMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private RequestCountMapper requestCountMapper;

    @Override
    public String getSitemap() {
        return "";
    }

    @Override
    public SiteCntVo getSiteCnt() {
        return querySiteVisitInfo(null, null);
    }

    @Override
    public SiteCntVo querySiteVisitInfo(LocalDate date, String path) {
        SiteCntVo vo = new SiteCntVo();
        
        // 查询已发布的文章数量
        Long articleCount = articleMapper.countAll();
        vo.setArticleCount(articleCount);
        
        // 查询用户总数
        Long userCount = userMapper.countAll();
        vo.setUserCount(userCount);
        
        // 查询评论总数
        Long commentCount = commentMapper.countAll();
        vo.setCommentCount(commentCount);
        
        // 查询访问册"
        if (date != null) {
            // 查询指定日期的访问量
            RequestCountDO requestCount = requestCountMapper.selectByDate(date);
            vo.setVisitCount(requestCount != null ? (long) requestCount.getCnt() : 0L);
        } else {
            // 查询总访问量
            List<RequestCountDO> requestCounts = requestCountMapper.selectList(null);
            long totalVisit = requestCounts.stream().mapToLong(RequestCountDO::getCnt).sum();
            vo.setVisitCount(totalVisit);
        }
        
        // 设置pv和uv（这里简化为使用总访问量册"
        vo.setPvCount(vo.getVisitCount());
        vo.setUvCount(vo.getVisitCount());
        
        return vo;
    }

    @Override
    public void saveVisitInfo(String clientIp, String path) {
    }

    @Override
    public SiteMapVo getSiteMap() {
        SiteMapVo vo = new SiteMapVo();
        vo.setLoc("");
        vo.setLastmod(LocalDate.now().toString());
        vo.setChangefreq("daily");
        vo.setPriority("1.0");
        return vo;
    }

    @Override
    public String getRobotsTxt() {
        return "User-agent: *\nAllow: /\nSitemap: /sitemap.xml";
    }

    @Override
    public void refreshSitemap() {
    }
}

