package com.example.forum.service.event;

import com.example.forum.core.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ArticlePublishEventListener {

    @Autowired
    private CacheService cacheService;

    @Async
    @EventListener
    public void onArticlePublish(ArticlePublishEvent event) {
        log.info("[异步事件] 文章发布事件触发: articleId={}, title={}", event.getArticleId(), event.getTitle());

        try {
            cacheService.invalidateArticleDetail(event.getArticleId());
            log.info("[异步事件] 已清除文章缓存: articleId={}", event.getArticleId());
        } catch (Exception e) {
            log.error("[异步事件] 清除缓存失败", e);
        }

        try {
            log.info("[异步事件] 通知关注者: authorId={}", event.getAuthorId());
            notifyFollowers(event);
        } catch (Exception e) {
            log.error("[异步事件] 通知粉丝失败", e);
        }

        try {
            log.info("[异步事件] 更新搜索索引: articleId={}", event.getArticleId());
            updateSearchIndex(event);
        } catch (Exception e) {
            log.error("[异步事件] 更新搜索索引失败", e);
        }

        log.info("[异步事件] 文章发布后处理完成, articleId={}", event.getArticleId());
    }

    private void notifyFollowers(ArticlePublishEvent event) {
        log.info("模拟通知{}的粉丝：发布了新文章「{}」", event.getAuthorName(), event.getTitle());
    }

    private void updateSearchIndex(ArticlePublishEvent event) {
        log.info("模拟更新ES搜索索引: articleId={}, title={}", event.getArticleId(), event.getTitle());
    }
}


