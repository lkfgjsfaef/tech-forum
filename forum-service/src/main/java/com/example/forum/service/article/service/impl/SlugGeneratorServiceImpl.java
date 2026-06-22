package com.example.forum.service.article.service.impl;

import com.example.forum.service.article.service.SlugGeneratorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SlugGeneratorServiceImpl implements SlugGeneratorService {

    @Override
    public String generateSlug(String title) {
        if (title == null || title.isEmpty()) {
            return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        }
        String slug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\u4e00-\\u9fa5\\s-]", "")
                .replaceAll("[\\s]+", "-")
                .replaceAll("-+", "-");
        if (slug.length() > 100) {
            slug = slug.substring(0, 100);
        }
        return slug.isEmpty() ? UUID.randomUUID().toString().replace("-", "").substring(0, 8) : slug;
    }

    @Override
    public String generateUniqueSlug(String title) {
        return generateSlug(title) + "-" + System.currentTimeMillis() % 10000;
    }
}
