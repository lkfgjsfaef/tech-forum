package com.example.forum.service.article.service;

public interface SlugGeneratorService {
    String generateSlug(String title);
    String generateUniqueSlug(String title);
}
