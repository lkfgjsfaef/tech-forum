package com.example.forum.service.article.service.impl;

import com.example.forum.service.article.entity.Article;
import com.example.forum.service.article.mapper.ArticleMapper;
import com.example.forum.service.article.service.ArticleSlugMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleSlugMigrationServiceImpl implements ArticleSlugMigrationService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public int migrateAllArticleSlugs() {
        List<Article> articles = articleMapper.findAll();
        int count = 0;
        for (Article article : articles) {
            if (article.getTitle() != null) {
                article.setUpdateTime(java.time.LocalDateTime.now());
                articleMapper.update(article);
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean regenerateSlug(Long articleId) {
        Article article = articleMapper.findById(articleId);
        if (article == null || article.getTitle() == null) {
            return false;
        }
        article.setUpdateTime(java.time.LocalDateTime.now());
        articleMapper.update(article);
        return true;
    }

    @Override
    public long countArticlesNeedMigration() {
        return articleMapper.findAll().size();
    }
}
