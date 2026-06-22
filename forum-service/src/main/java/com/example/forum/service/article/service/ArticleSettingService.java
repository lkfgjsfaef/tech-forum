package com.example.forum.service.article.service;

import com.example.forum.api.model.enums.OperateArticleEnum;
import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.article.AiSlugGenerateReq;
import com.example.forum.api.model.vo.article.ArticlePostReq;
import com.example.forum.api.model.vo.article.SearchArticleReq;
import com.example.forum.api.model.vo.article.dto.ArticleAdminDTO;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import org.springframework.stereotype.Service;

@Service
public interface ArticleSettingService {
    PageVo<ArticleAdminDTO> getArticleList(SearchArticleReq req);
    ArticleDTO getArticleDetail(Long articleId);
    void saveArticle(ArticleDTO article);
    void deleteArticle(Long articleId);
    void updateArticle(ArticlePostReq req);
    void operateArticle(Long articleId, OperateArticleEnum operate);
    String generateUrlSlug(AiSlugGenerateReq req);
}
