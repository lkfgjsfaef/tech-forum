package com.example.forum.service.user.service.impl;

import com.example.forum.service.user.service.AuthorWhiteListService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthorWhiteListServiceImpl implements AuthorWhiteListService {

    private Set<Long> whiteList = new HashSet<>();

    @Override
    public boolean isInWhiteList(Long userId) {
        return whiteList.contains(userId);
    }

    public void addAuthor2ArticleWhitList(Long authorId) {
        whiteList.add(authorId);
    }

    public void removeAuthorFromArticleWhiteList(Long authorId) {
        whiteList.remove(authorId);
    }

    public java.util.List<com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO> queryAllArticleWhiteListAuthors() {
        return java.util.Collections.emptyList();
    }
}
