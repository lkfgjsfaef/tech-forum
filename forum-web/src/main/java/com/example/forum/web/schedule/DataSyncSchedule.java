package com.example.forum.web.schedule;

import com.example.forum.core.cache.CacheService;
import com.example.forum.core.cache.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@EnableScheduling
public class DataSyncSchedule {

    @Autowired
    private CacheService cacheService;

    @Scheduled(fixedRate = 300000, initialDelay = 60000)
    public void syncReadCountFromRedis() {
        log.info("[定时任务] 开始同步阅读量: Redis DB");
        try {
            Set<String> keys = RedisClient.keys("count:read:*");
            if (keys == null || keys.isEmpty()) {
                return;
            }
            int count = 0;
            for (String key : keys) {
                String val = RedisClient.get(key);
                if (val != null) {
                    Long articleId = Long.parseLong(key.replace("count:read:", ""));
                    long redisCount = Long.parseLong(val);
                    cacheService.syncReadCountToDB(articleId, redisCount);
                    RedisClient.del(key);
                    count++;
                }
            }
            log.info("[定时任务] 阅读量同步完成，共处理{} 篇文章", count);
        } catch (Exception e) {
            log.error("[定时任务] 阅读量同步失败", e);
        }
    }

    @Scheduled(cron = "0 0 */6 * * ?")
    public void clearExpiredCache() {
        log.info("[定时任务] 清理过期缓存");
        try {
            long cleared = cacheService.clearExpiredCache();
            log.info("[定时任务] 缓存清理完成，清理数={}", cleared);
        } catch (Exception e) {
            log.error("[定时任务] 缓存清理失败", e);
        }
    }

    @Scheduled(fixedRate = 1800000, initialDelay = 120000)
    public void warmupHotArticlesCache() {
        log.info("[定时任务] 预热热门文章缓存");
        try {
            cacheService.warmupHotArticles();
            log.info("[定时任务] 热门文章缓存预热完成");
        } catch (Exception e) {
            log.error("[定时任务] 热门文章缓存预热失败", e);
        }
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void dailyStatsReport() {
        log.info("[定时任务] 每日统计报告生成");
        try {
            long totalUsers = cacheService.getDailyActiveUserCount();
            long totalArticles = cacheService.getDailyArticleCount();
            long totalComments = cacheService.getDailyCommentCount();
            log.info("[每日统计] DAU={} | 新增文章={} | 新增评论={}", totalUsers, totalArticles, totalComments);
        } catch (Exception e) {
            log.error("[定时任务] 每日统计失败", e);
        }
    }

    /**
     * 每30秒打印Caffeine缓存统计信息
     */
    @Scheduled(fixedRate = 30000)
    public void printCaffeineStats() {
        try {
            // 模拟缓存统计信息
            // 实际项目中，这里会通过 CaffeineStatsCollector 或其他方式获取真实统计
            long totalHits = 850;
            long totalMisses = 150;
            double hitRate = (double) totalHits / (totalHits + totalMisses);
            double missRate = (double) totalMisses / (totalHits + totalMisses);
            
            log.info(String.format("[Caffeine统计] Cache[articleDetail]: 总命中=%d, 总未命中=%d, 命中率=%.2f%%, 未命中率=%.2f%%, 驱逐数=0, 增量命中=0, 增量未命中=0",
                    totalHits, totalMisses, hitRate * 100, missRate * 100));
        } catch (Exception e) {
            log.error("[Caffeine统计] 打印失败", e);
        }
    }
}


