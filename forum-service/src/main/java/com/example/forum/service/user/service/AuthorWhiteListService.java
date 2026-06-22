package com.example.forum.service.user.service;

import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;

import java.util.List;

public interface AuthorWhiteListService {
    boolean isInWhiteList(Long userId);
    void addAuthor2ArticleWhitList(Long authorId);
    void removeAuthorFromArticleWhiteList(Long authorId);
    List<BaseUserInfoDTO> queryAllArticleWhiteListAuthors();
}
