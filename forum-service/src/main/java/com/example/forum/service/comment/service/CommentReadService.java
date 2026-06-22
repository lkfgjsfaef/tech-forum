package com.example.forum.service.comment.service;

import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.comment.vo.SubCommentListVO;
import com.example.forum.api.model.vo.comment.dto.TopCommentDTO;
import com.example.forum.service.comment.repository.entity.CommentDO;

import java.util.List;

public interface CommentReadService {
    List<TopCommentDTO> getCommentList(Long articleId, Long userId);
    List<TopCommentDTO> getArticleComments(Long articleId, PageParam pageParam);
    int queryTopCommentCount(Long articleId);
    TopCommentDTO queryHotComment(Long articleId);
    List<TopCommentDTO> queryHighlightComments(Long articleId);
    TopCommentDTO queryTopComments(Long commentId);
    CommentDO queryComment(Long commentId);
    SubCommentListVO getSubComments(Long topCommentId, PageParam pageParam);
}
