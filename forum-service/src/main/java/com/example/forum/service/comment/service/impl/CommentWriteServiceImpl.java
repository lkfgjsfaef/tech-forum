package com.example.forum.service.comment.service.impl;

import com.example.forum.api.model.vo.comment.CommentSaveReq;
import com.example.forum.service.comment.entity.Comment;
import com.example.forum.service.comment.mapper.CommentMapper;
import com.example.forum.service.comment.service.CommentWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentWriteServiceImpl implements CommentWriteService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveComment(CommentSaveReq req) {
        Comment comment = new Comment();
        comment.setArticleId(req.getArticleId());
        comment.setUserId(req.getUserId());
        comment.setContent(req.getCommentContent() != null ? req.getCommentContent() : req.getContent());

        // 处理层级评论
        if (req.getParentId() != null && req.getParentId() > 0) {
            comment.setParentCommentId(req.getParentId());
            // 查找顶级评论ID
            Comment parentComment = commentMapper.findById(req.getParentId());
            if (parentComment != null) {
                if (parentComment.getTopCommentId() != null && parentComment.getTopCommentId() > 0) {
                    comment.setTopCommentId(parentComment.getTopCommentId());
                } else {
                    // 父评论本身就是顶级评册"
                    comment.setTopCommentId(parentComment.getId());
                }
            }
        } else {
            comment.setParentCommentId(0L);
            comment.setTopCommentId(0L);
        }

        commentMapper.insert(comment);
        return comment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId) {
        commentMapper.logicDelete(commentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment != null && comment.getUserId().equals(userId)) {
            commentMapper.logicDelete(commentId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeComment(Long commentId, Long userId) {
        // 点赞评论的逻辑通过 UserFootService 册"favorArticleComment 方法处理
        // 此方法为扩展预留
    }
}

