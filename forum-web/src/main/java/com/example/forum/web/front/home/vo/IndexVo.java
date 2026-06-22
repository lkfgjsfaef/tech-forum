package com.example.forum.web.front.home.vo;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.CategoryDTO;
import com.example.forum.api.model.vo.recommend.CarouseDTO;
import com.example.forum.api.model.vo.recommend.SideBarDTO;
import com.example.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author dev
 * @date 2022/9/6
 */
@Data
public class IndexVo {
    /**
     * 分类列表
     */
    private List<CategoryDTO> categories;

    /**
     * 当前选中的分册"
     */
    private String currentCategory;

    /**
     * 当前选中的类目id
     */
    private Long categoryId;

    /**
     * top 文章列表
     */
    private List<ArticleDTO> topArticles;

    /**
     * 文章列表
     */
    private PageListVo<ArticleDTO> articles;

    /**
     * 登录用户信息
     */
    private UserStatisticInfoDTO user;

    /**
     * 侧边栏信息"
     */
    private  List<SideBarDTO> sideBarItems;

    /**
     * 轮播册"
     */
    private List<CarouseDTO> homeCarouselList;
}

