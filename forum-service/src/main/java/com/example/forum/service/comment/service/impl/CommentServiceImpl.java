package com.example.forum.service.comment.service.impl;

import com.example.forum.service.article.service.ArticleService;
import com.example.forum.service.comment.entity.Comment;
import com.example.forum.service.comment.mapper.CommentMapper;
import com.example.forum.service.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleService articleService;

    @Override
    public void create(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        commentMapper.insert(comment);
        // 增加文章评论数
        articleService.incrementCommentCount(comment.getArticleId());
    }

    @Override
    public void update(Comment comment) {
        comment.setUpdateTime(LocalDateTime.now());
        commentMapper.update(comment);
    }

    @Override
    public Comment findById(Long id) {
        return commentMapper.findById(id);
    }

    @Override
    public List<Comment> findByArticleId(Long articleId) {
        return commentMapper.findByArticleId(articleId);
    }

    @Override
    public List<Comment> findByUserId(Long userId) {
        return commentMapper.findByUserId(userId);
    }
}

