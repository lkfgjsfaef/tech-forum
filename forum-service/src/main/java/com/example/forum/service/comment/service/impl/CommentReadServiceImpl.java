package com.example.forum.service.comment.service.impl;

import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.comment.dto.TopCommentDTO;
import com.example.forum.api.model.vo.comment.vo.SubCommentListVO;
import com.example.forum.service.comment.entity.Comment;
import com.example.forum.service.comment.mapper.CommentMapper;
import com.example.forum.service.comment.repository.entity.CommentDO;
import com.example.forum.service.comment.service.CommentReadService;
import com.example.forum.service.user.entity.User;
import com.example.forum.service.user.mapper.UserInfoMapper;
import com.example.forum.service.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentReadServiceImpl implements CommentReadService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public List<TopCommentDTO> getCommentList(Long articleId, Long userId) {
        return getArticleComments(articleId, PageParam.newPageInstance());
    }

    @Override
    public List<TopCommentDTO> getArticleComments(Long articleId, PageParam pageParam) {
        List<Comment> topComments = commentMapper.findTopCommentsByArticleId(
                articleId, pageParam.getOffset(), pageParam.getPageSize());
        List<TopCommentDTO> result = new ArrayList<>();
        for (Comment topComment : topComments) {
            TopCommentDTO dto = convertToTopCommentDTO(topComment);
            // 加载子评论（册"条）
            List<Comment> subComments = commentMapper.findSubCommentsByTopCommentId(
                    topComment.getId(), 0, PageParam.TOP_PAGE_SIZE);
            List<TopCommentDTO.SubCommentDTO> subDTOs = new ArrayList<>();
            for (Comment sub : subComments) {
                subDTOs.add(convertToSubCommentDTO(sub));
            }
            dto.setSubComments(subDTOs);
            result.add(dto);
        }
        return result;
    }

    @Override
    public int queryTopCommentCount(Long articleId) {
        return commentMapper.countTopCommentsByArticleId(articleId);
    }

    @Override
    public TopCommentDTO queryHotComment(Long articleId) {
        // 简单实现：返回第一个顶级评论作为热门评册"
        List<Comment> topComments = commentMapper.findTopCommentsByArticleId(articleId, 0, 1);
        if (topComments.isEmpty()) {
            return null;
        }
        return convertToTopCommentDTO(topComments.get(0));
    }

    @Override
    public List<TopCommentDTO> queryHighlightComments(Long articleId) {
        // 简单实现：返回册"个顶级评册"
        List<Comment> topComments = commentMapper.findTopCommentsByArticleId(articleId, 0, 3);
        List<TopCommentDTO> result = new ArrayList<>();
        for (Comment comment : topComments) {
            result.add(convertToTopCommentDTO(comment));
        }
        return result;
    }

    @Override
    public TopCommentDTO queryTopComments(Long commentId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) {
            return null;
        }
        TopCommentDTO dto = convertToTopCommentDTO(comment);
        // 加载子评册"
        List<Comment> subComments = commentMapper.findSubCommentsByTopCommentId(commentId, 0, 10);
        List<TopCommentDTO.SubCommentDTO> subDTOs = new ArrayList<>();
        for (Comment sub : subComments) {
            subDTOs.add(convertToSubCommentDTO(sub));
        }
        dto.setSubComments(subDTOs);
        return dto;
    }

    @Override
    public CommentDO queryComment(Long commentId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) {
            return null;
        }
        CommentDO commentDO = new CommentDO();
        commentDO.setId(comment.getId());
        commentDO.setArticleId(comment.getArticleId());
        commentDO.setUserId(comment.getUserId());
        commentDO.setContent(comment.getContent());
        commentDO.setParentId(comment.getParentCommentId());
        commentDO.setCreateTime(comment.getCreateTime() != null ?
                comment.getCreateTime().toEpochSecond(java.time.ZoneOffset.ofHours(8)) : null);
        commentDO.setUpdateTime(comment.getUpdateTime() != null ?
                comment.getUpdateTime().toEpochSecond(java.time.ZoneOffset.ofHours(8)) : null);
        return commentDO;
    }

    @Override
    public SubCommentListVO getSubComments(Long topCommentId, PageParam pageParam) {
        SubCommentListVO vo = new SubCommentListVO();
        vo.setParentId(topCommentId);
        List<Comment> subComments = commentMapper.findSubCommentsByTopCommentId(
                topCommentId, pageParam.getOffset(), pageParam.getPageSize());
        List<SubCommentListVO.SubCommentItem> items = new ArrayList<>();
        for (Comment sub : subComments) {
            SubCommentListVO.SubCommentItem item = new SubCommentListVO.SubCommentItem();
            item.setId(sub.getId());
            item.setContent(sub.getContent());
            item.setCreateTime(sub.getCreateTime() != null ?
                    sub.getCreateTime().toEpochSecond(java.time.ZoneOffset.ofHours(8)) : null);
            User user = userMapper.findById(sub.getUserId());
            if (user != null) {
                String nickName = userInfoMapper.queryNickNameByUserId(user.getId());
                item.setUserName((nickName != null && !nickName.isEmpty()) ? nickName : user.getUsername());
            } else {
                item.setUserName("未知用户");
            }
            items.add(item);
        }
        vo.setComments(items);
        int total = commentMapper.countSubCommentsByTopCommentId(topCommentId);
        vo.setHasMore((long) pageParam.getPageNum() * pageParam.getPageSize() < total);
        return vo;
    }

    private TopCommentDTO convertToTopCommentDTO(Comment comment) {
        TopCommentDTO dto = new TopCommentDTO();
        dto.setId(comment.getId());
        dto.setArticleId(comment.getArticleId());
        dto.setUserId(comment.getUserId());
        dto.setContent(comment.getContent());
        dto.setCreateTime(comment.getCreateTime() != null ?
                comment.getCreateTime().toEpochSecond(java.time.ZoneOffset.ofHours(8)) : null);
        // 查询用户信息
        User user = userMapper.findById(comment.getUserId());
        if (user != null) {
            String nickName = userInfoMapper.queryNickNameByUserId(user.getId());
            dto.setUserName((nickName != null && !nickName.isEmpty()) ? nickName : user.getUsername());
            dto.setUserAvatar(user.getAvatar());
        }
        // 查询点赞册"
        dto.setLikeCount(0); // 默认值，后续可通过 user_foot 统计
        return dto;
    }

    private TopCommentDTO.SubCommentDTO convertToSubCommentDTO(Comment comment) {
        TopCommentDTO.SubCommentDTO dto = new TopCommentDTO.SubCommentDTO();
        dto.setId(comment.getId());
        dto.setParentId(comment.getParentCommentId());
        dto.setUserId(comment.getUserId());
        dto.setContent(comment.getContent());
        dto.setCreateTime(comment.getCreateTime() != null ?
                comment.getCreateTime().toEpochSecond(java.time.ZoneOffset.ofHours(8)) : null);
        User user = userMapper.findById(comment.getUserId());
        if (user != null) {
            String nickName = userInfoMapper.queryNickNameByUserId(user.getId());
            dto.setUserName((nickName != null && !nickName.isEmpty()) ? nickName : user.getUsername());
            dto.setUserAvatar(user.getAvatar());
        }
        return dto;
    }
}

