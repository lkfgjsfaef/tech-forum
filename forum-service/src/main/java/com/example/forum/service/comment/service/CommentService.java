package com.example.forum.service.comment.service;

import com.example.forum.service.comment.entity.Comment;

import java.util.List;

public interface CommentService {
    void create(Comment comment);
    void update(Comment comment);
    Comment findById(Long id);
    List<Comment> findByArticleId(Long articleId);
    List<Comment> findByUserId(Long userId);
}
