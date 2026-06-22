package com.example.forum.service.sidebar.service;

import com.example.forum.api.model.vo.recommend.SideBarDTO;

import java.util.List;

public interface SidebarService {
    List<SideBarDTO> queryHomeSidebarList();
    List<SideBarDTO> queryArticleSidebarList();
    List<SideBarDTO> queryArticleDetailSidebarList(Long author, Long articleId);
    List<SideBarDTO> queryColumnSidebarList();
}
