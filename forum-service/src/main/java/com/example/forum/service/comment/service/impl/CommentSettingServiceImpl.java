package com.example.forum.service.comment.service.impl;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.comment.CommentSaveReq;
import com.example.forum.api.model.vo.comment.SearchCommentReq;
import com.example.forum.api.model.vo.comment.dto.CommentAdminDTO;
import com.example.forum.service.comment.service.CommentSettingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CommentSettingServiceImpl implements CommentSettingService {

    @Override
    public PageVo<CommentAdminDTO> getCommentList(SearchCommentReq req) {
        PageVo<CommentAdminDTO> result = new PageVo<>();
        result.setList(new ArrayList<>());
        result.setTotal(0L);
        return result;
    }

    @Override
    public CommentAdminDTO getCommentDetail(Long commentId) {
        return new CommentAdminDTO();
    }

    @Override
    public void saveComment(CommentSaveReq req, Long userId) {
    }

    @Override
    public void deleteComment(Long commentId) {
    }
}
