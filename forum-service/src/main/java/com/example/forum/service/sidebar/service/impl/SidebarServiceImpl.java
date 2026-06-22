package com.example.forum.service.sidebar.service.impl;

import com.example.forum.api.model.vo.recommend.SideBarDTO;
import com.example.forum.service.article.repository.dao.TagDao;
import com.example.forum.service.article.repository.entity.TagDO;
import com.example.forum.service.sidebar.service.SidebarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SidebarServiceImpl implements SidebarService {

    @Autowired
    private TagDao tagDao;

    @Override
    public List<SideBarDTO> queryHomeSidebarList() {
        List<TagDO> tags = tagDao.findAll();
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        return tags.stream().map(tag -> {
            SideBarDTO dto = new SideBarDTO();
            dto.setTitle(tag.getTag());
            dto.setType("tag");
            dto.setContent(tag.getArticleCount() + " 篇文册");
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SideBarDTO> queryArticleSidebarList() {
        return queryHomeSidebarList();
    }

    @Override
    public List<SideBarDTO> queryArticleDetailSidebarList(Long author, Long articleId) {
        return queryHomeSidebarList();
    }

    @Override
    public List<SideBarDTO> queryColumnSidebarList() {
        List<SideBarDTO> items = new ArrayList<>();
        // 添加推荐专栏
        SideBarDTO item1 = new SideBarDTO();
        item1.setTitle("推荐阅读");
        item1.setContent("Tech Forum系列教程正在连载册");
        item1.setType("recommend");
        item1.setHref("/column/list");
        items.add(item1);

        SideBarDTO item2 = new SideBarDTO();
        item2.setTitle("热门标签");
        item2.setContent("点击查看所有标册");
        item2.setType("tag");
        item2.setHref("/article/tag/java");
        items.add(item2);

        SideBarDTO item3 = new SideBarDTO();
        item3.setTitle("活跃用户");
        item3.setContent("查看贡献排行册");
        item3.setType("rank");
        item3.setHref("/rank/month");
        items.add(item3);
        return items;
    }
}

