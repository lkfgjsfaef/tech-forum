package com.example.forum.web.front.article.vo;

import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.ArticleOtherDTO;
import com.example.forum.api.model.vo.article.dto.ArticlePayInfoDTO;
import com.example.forum.api.model.vo.comment.dto.TopCommentDTO;
import com.example.forum.api.model.vo.recommend.SideBarDTO;
import com.example.forum.api.model.vo.user.dto.SimpleUserInfoDTO;
import com.example.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author dev
 * @date 2022/9/2
 */
@Data
public class ArticleDetailVo {
    /**
     * 文章信息
     */
    private ArticleDTO article;

    /**
     * 评论信息
     */
    private List<TopCommentDTO> comments;

    /**
     * 一级评论总数
     */
    private Integer topCommentTotal;

    /**
     * 热门评论
     */
    private TopCommentDTO hotComment;

    /**
     * 划线引用评论
     */
    private List<TopCommentDTO> highlightComments;

    /**
     * 作者相关信息"
     */
    private UserStatisticInfoDTO author;


    private ArticlePayInfoDTO payInfo;

    // 其他的信息，比如说翻页，比如说阅读类册"
    private ArticleOtherDTO other;

    /**
     * 侧边栏信息"
     */
    private List<SideBarDTO> sideBarItems;


    /**
     * 打赏用户列表
     */
    private List<SimpleUserInfoDTO> payUsers;
}

