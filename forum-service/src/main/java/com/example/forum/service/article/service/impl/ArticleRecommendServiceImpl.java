package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.service.article.service.ArticleRecommendService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleRecommendServiceImpl implements ArticleRecommendService {

    @Override
    public List<ArticleDTO> queryRecommendArticles(Long articleId, int size) {
        return new ArrayList<>();
    }

    @Override
    public List<ArticleDTO> queryHotArticles(int size) {
        return new ArrayList<>();
    }

    @Override
    public PageListVo<ArticleDTO> relatedRecommend(Long articleId, PageParam pageParam) {
        PageListVo<ArticleDTO> result = new PageListVo<>();
        result.setList(new ArrayList<>());
        result.setHasMore(false);
        return result;
    }
}
