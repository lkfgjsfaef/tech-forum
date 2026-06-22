package com.example.forum.service.comment.service;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.comment.CommentSaveReq;
import com.example.forum.api.model.vo.comment.SearchCommentReq;
import com.example.forum.api.model.vo.comment.dto.CommentAdminDTO;

public interface CommentSettingService {
    PageVo<CommentAdminDTO> getCommentList(SearchCommentReq req);
    CommentAdminDTO getCommentDetail(Long commentId);
    void saveComment(CommentSaveReq req, Long userId);
    void deleteComment(Long commentId);
}
