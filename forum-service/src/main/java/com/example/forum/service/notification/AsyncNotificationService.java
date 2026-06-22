package com.example.forum.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncNotificationService {

    @Async
    public void notifyArticleLiked(Long articleId, Long authorId, Long likerId) {
        log.info("[异步通知] 用户{}点赞了文章{}, 通知作者{}", likerId, articleId, authorId);
        try {
            Thread.sleep(100);
            log.info("[异步通知] 点赞通知已发送: articleId={}", articleId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Async
    public void notifyArticleCollected(Long articleId, Long authorId, Long collectorId) {
        log.info("[异步通知] 用户{}收藏了文章{}, 通知作者{}", collectorId, articleId, authorId);
        try {
            Thread.sleep(100);
            log.info("[异步通知] 收藏通知已发送: articleId={}", articleId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Async
    public void notifyNewComment(Long articleId, Long authorId, Long commenterId, String content) {
        log.info("[异步通知] 用户{}评论了文章{}, 通知作者", commenterId, articleId);
        try {
            Thread.sleep(100);
            log.info("[异步通知] 评论通知已发送: articleId={}", articleId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Async
    public void notifyUserFollowed(Long followerId, Long followingId) {
        log.info("[异步通知] 用户{}关注了用户{}", followerId, followingId);
        try {
            Thread.sleep(100);
            log.info("[异步通知] 关注通知已发送");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

