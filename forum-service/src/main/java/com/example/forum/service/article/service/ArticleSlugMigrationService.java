package com.example.forum.service.article.service;

public interface ArticleSlugMigrationService {
    int migrateAllArticleSlugs();
    boolean regenerateSlug(Long articleId);
    long countArticlesNeedMigration();
}
