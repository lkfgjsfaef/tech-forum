package com.example.forum.service.comment.service;

import com.example.forum.api.model.vo.comment.CommentSaveReq;
import com.example.forum.api.model.vo.comment.dto.TopCommentDTO;

import java.util.List;

public interface CommentWriteService {
    Long saveComment(CommentSaveReq req);
    void deleteComment(Long commentId);
    void deleteComment(Long commentId, Long userId);
    void likeComment(Long commentId, Long userId);
}
