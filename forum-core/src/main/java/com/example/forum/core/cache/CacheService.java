package com.example.forum.core.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CacheService {

    private static final String ARTICLE_DETAIL_PREFIX = "article:detail:";
    private static final String HOT_ARTICLES_PREFIX = "hot:articles:";
    private static final String RANKING_PREFIX = "ranking:";
    private static final String ARTICLE_READ_PREFIX = "read:";
    private static final String USER_COLLECT_PREFIX = "collect:";

    public void cacheArticleDetail(Long articleId, Object data) {
        try {
            String key = ARTICLE_DETAIL_PREFIX + articleId;
            RedisClient.set(key, data.toString());
            RedisClient.expire(key, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("缓存文章详情失败: articleId={}", articleId, e);
        }
    }

    public String getArticleDetail(Long articleId) {
        return RedisClient.get(ARTICLE_DETAIL_PREFIX + articleId);
    }

    public void invalidateArticleDetail(Long articleId) {
        RedisClient.del(ARTICLE_DETAIL_PREFIX + articleId);
    }

    public void cacheHotArticles(String categoryKey, List<?> articles) {
        try {
            String key = HOT_ARTICLES_PREFIX + categoryKey;
            RedisClient.set(key, articles.toString());
            RedisClient.expire(key, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("缓存热门文章失败", e);
        }
    }

    public String getHotArticles(String categoryKey) {
        return RedisClient.get(HOT_ARTICLES_PREFIX + categoryKey);
    }

    public boolean isArticleReadByUser(Long articleId, Long userId) {
        if (userId == null || articleId == null) return false;
        String key = ARTICLE_READ_PREFIX + userId;
        Boolean exists = RedisClient.sismember(key, String.valueOf(articleId));
        if (exists == null || !exists) {
            RedisClient.sadd(key, String.valueOf(articleId));
            RedisClient.expire(key, 24, TimeUnit.HOURS);
            return false;
        }
        return true;
    }

    public long incrementReadCount(Long articleId) {
        String key = "count:read:" + articleId;
        Long count = RedisClient.incr(key);
        if (count != null && count == 1) {
            RedisClient.expire(key, 24, TimeUnit.HOURS);
        }
        return count != null ? count : 0;
    }

    public long getReadCountFromCache(Long articleId) {
        String val = RedisClient.get("count:read:" + articleId);
        if (val != null) {
            try {
                return Long.parseLong(val);
            } catch (NumberFormatException ignored) {}
        }
        return -1;
    }

    public boolean isCollected(Long userId, Long articleId) {
        if (userId == null) return false;
        String key = USER_COLLECT_PREFIX + userId;
        Boolean exists = RedisClient.sismember(key, String.valueOf(articleId));
        return exists != null && exists;
    }

    public void setCollect(Long userId, Long articleId, boolean collected) {
        String key = USER_COLLECT_PREFIX + userId;
        if (collected) {
            RedisClient.sadd(key, String.valueOf(articleId));
        } else {
            RedisClient.srem(key, String.valueOf(articleId));
        }
        RedisClient.expire(key, 7, TimeUnit.DAYS);
    }

    public void cacheRanking(String type, List<?> data) {
        try {
            String key = RANKING_PREFIX + type;
            RedisClient.set(key, data.toString());
            RedisClient.expire(key, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("缓存排行榜失败, type={}", type, e);
        }
    }

    public String getRanking(String type) {
        return RedisClient.get(RANKING_PREFIX + type);
    }

    public void syncReadCountToDB(Long articleId, long redisCount) {
        log.info("同步文章阅读量, articleId={}, redisCount={}", articleId, redisCount);
    }

    public long clearExpiredCache() {
        log.info("清理过期缓存");
        return 0;
    }

    public void warmupHotArticles() {
        log.info("预热热门文章缓存");
    }

    public long getDailyActiveUserCount() {
        return 0L;
    }

    public long getDailyArticleCount() {
        return 0L;
    }

    public long getDailyCommentCount() {
        return 0L;
    }
}


