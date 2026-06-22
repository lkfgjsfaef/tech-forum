package com.example.forum.api.model.vo.article.dto;

import com.example.forum.api.model.vo.comment.dto.TopCommentDTO;
import com.example.forum.api.model.vo.user.dto.SimpleUserInfoDTO;
import com.example.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import lombok.Data;
import java.util.List;

@Data
public class ColumnArticlesDTO {
    private ColumnDTO column;
    private ArticleDTO article;
    private UserStatisticInfoDTO author;
    private List<ArticleDTO> articles;
    private List<TopCommentDTO> comments;
    private Integer topCommentTotal;
    private TopCommentDTO hotComment;
    private List<TopCommentDTO> highlightComments;
    private Long columnId;
    private Integer section;
    private List<SimpleArticleDTO> articleList;
    private ArticleOtherDTO other;
    private List<SimpleUserInfoDTO> payUsers;
}
