package com.example.forum.web.front.article.rest;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.constants.StatusEnum;
import com.example.forum.core.aop.OperationLog;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.article.service.ArticleCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("article/api/collect")
public class ArticleCollectRestController {

    @Autowired
    private ArticleCollectService articleCollectService;

    @Permission(role = UserRole.LOGIN)
    @OperationLog(value = "切换收藏", module = "文章收藏")
    @PostMapping("/toggle")
    public ResVo<Map<String, Object>> toggle(@RequestBody Map<String, Object> params) {
        try {
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            Long articleId = Long.parseLong(params.get("articleId").toString());
            boolean isCollected = articleCollectService.isCollected(userId, articleId);

            if (isCollected) {
                articleCollectService.uncollect(userId, articleId);
                log.info("取消收藏: userId={}, articleId={}", userId, articleId);
            } else {
                articleCollectService.collect(userId, articleId);
                log.info("收藏成功: userId={}, articleId={}", userId, articleId);
            }

            int count = articleCollectService.getCollectCount(articleId);
            Map<String, Object> result = new HashMap<>();
            result.put("collected", !isCollected);
            result.put("count", count);
            return ResVo.ok(result);
        } catch (Exception e) {
            log.error("收藏操作失败", e);
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "操作失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/{articleId}")
    public ResVo<Map<String, Object>> check(@PathVariable Long articleId) {
        try {
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            boolean isCollected = false;
            if (userId != null) {
                isCollected = articleCollectService.isCollected(userId, articleId);
            }
            int count = articleCollectService.getCollectCount(articleId);
            Map<String, Object> result = new HashMap<>();
            result.put("collected", isCollected);
            result.put("count", count);
            return ResVo.ok(result);
        } catch (Exception e) {
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "查询失败");
        }
    }

    @Permission(role = UserRole.LOGIN)
    @GetMapping("/my")
    public ResVo<PageListVo<ArticleDTO>> myCollections(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        try {
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            PageListVo<ArticleDTO> result = articleCollectService.queryUserCollections(
                    userId, PageParam.newPageInstance(pageNum, pageSize));
            return ResVo.ok(result);
        } catch (Exception e) {
            log.error("获取收藏列表失败", e);
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "获取失败");
        }
    }

    @GetMapping("/ids")
    public ResVo<List<Long>> myCollectionIds() {
        try {
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            if (userId == null) {
                return ResVo.ok(new java.util.ArrayList<>());
            }
            List<Long> ids = articleCollectService.getUserCollectedArticleIds(userId);
            return ResVo.ok(ids);
        } catch (Exception e) {
            return ResVo.ok(new java.util.ArrayList<>());
        }
    }
}
