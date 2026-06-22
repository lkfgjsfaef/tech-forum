package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.enums.OperateArticleEnum;
import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.article.AiSlugGenerateReq;
import com.example.forum.api.model.vo.article.ArticlePostReq;
import com.example.forum.api.model.vo.article.SearchArticleReq;
import com.example.forum.api.model.vo.article.dto.ArticleAdminDTO;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.service.article.service.ArticleSettingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ArticleSettingServiceImpl implements ArticleSettingService {

    @Override
    public PageVo<ArticleAdminDTO> getArticleList(SearchArticleReq req) {
        PageVo<ArticleAdminDTO> result = new PageVo<>();
        result.setList(new ArrayList<>());
        result.setTotal(0L);
        return result;
    }

    @Override
    public ArticleDTO getArticleDetail(Long articleId) {
        return new ArticleDTO();
    }

    @Override
    public void saveArticle(ArticleDTO article) {
    }

    @Override
    public void deleteArticle(Long articleId) {
    }

    @Override
    public void updateArticle(ArticlePostReq req) {
    }

    @Override
    public void operateArticle(Long articleId, OperateArticleEnum operate) {
    }

    @Override
    public String generateUrlSlug(AiSlugGenerateReq req) {
        return "";
    }
}
