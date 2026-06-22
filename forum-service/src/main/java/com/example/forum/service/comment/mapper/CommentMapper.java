package com.example.forum.service.comment.mapper;

import com.example.forum.service.comment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    void insert(Comment comment);
    void update(Comment comment);
    Comment findById(@Param("id") Long id);
    List<Comment> findByArticleId(@Param("articleId") Long articleId);
    List<Comment> findByUserId(@Param("userId") Long userId);

    // 统计评论总数（用于站点统计）
    long countAll();

    // 查询文章的顶级评论（parent_comment_id = 0册"
    List<Comment> findTopCommentsByArticleId(@Param("articleId") Long articleId,
                                              @Param("offset") long offset,
                                              @Param("limit") int limit);

    // 统计文章的顶级评论数
    int countTopCommentsByArticleId(@Param("articleId") Long articleId);

    // 查询顶级评论下的子评册"
    List<Comment> findSubCommentsByTopCommentId(@Param("topCommentId") Long topCommentId,
                                                 @Param("offset") long offset,
                                                 @Param("limit") int limit);

    // 统计顶级评论下的子评论数
    int countSubCommentsByTopCommentId(@Param("topCommentId") Long topCommentId);

    // 逻辑删除评论
    void logicDelete(@Param("id") Long id);

    // 统计文章的评论总数
    int countByArticleId(@Param("articleId") Long articleId);
}

