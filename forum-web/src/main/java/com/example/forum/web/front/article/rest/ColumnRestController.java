package com.example.forum.web.front.article.rest;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.constants.StatusEnum;
import com.example.forum.api.model.vo.article.dto.ColumnDTO;
import com.example.forum.core.aop.OperationLog;
import com.example.forum.service.article.entity.Article;
import com.example.forum.service.article.repository.entity.ColumnInfoDO;
import com.example.forum.service.article.service.ColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 专栏相关REST接口
 */
@Slf4j
@RestController
@RequestMapping(path = {"/column/api"})
public class ColumnRestController {

    @Autowired
    private ColumnService columnService;

    /**
     * 保存/更新专栏
     * POST /column/api/save
     *
     * @param params 专栏参数（id, columnName, introduction, cover, nums, type）
     * @return 专栏ID
     */
    @OperationLog(value = "保存/创建专栏", module = "专栏管理")
    @PostMapping("/save")
    public ResVo<Long> save(@RequestBody java.util.Map<String, Object> params) {
        try {
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            
            String columnName = (String) params.get("columnName");
            String introduction = (String) params.get("introduction");
            String cover = (String) params.get("cover");
            if (cover == null) {
                cover = "";
            }
            
            // 处理封面图片：如果太长就截断
            if (cover != null && cover.length() > 120) {
                log.warn("封面图片URL过长({}字符)，将被截断", cover.length());
                // 如果是base64数据，直接清空（base64不适合存这个字段）
                if (cover.startsWith("data:image")) {
                    cover = "";  // 清空，不存base64
                } else {
                    // 如果是URL，截取前120个字符
                    cover = cover.substring(0, 120);
                }
            }
            
            Integer nums = params.get("nums") != null ? ((Number) params.get("nums")).intValue() : 10;
            Integer type = params.get("type") != null ? ((Number) params.get("type")).intValue() : 0;
            
            if (columnName == null || columnName.trim().isEmpty()) {
                return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "专栏名称不能为空");
            }
            
            if (introduction == null || introduction.trim().isEmpty()) {
                return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "专栏简介不能为空");
            }

            ColumnDTO columnDTO = new ColumnDTO();
            columnDTO.setColumnName(columnName.trim());
            columnDTO.setIntroduction(introduction.trim());
            columnDTO.setCover(cover);
            columnDTO.setNums(nums);
            columnDTO.setType(type);
            
            // 如果有ID，则是编辑模式
            if (params.containsKey("id") && params.get("id") != null) {
                Long columnId = Long.parseLong(params.get("id").toString());
                columnDTO.setColumnId(columnId);
                
                // 验证权限：只有作者本人才能编辑
                ColumnDTO existing = columnService.queryColumnInfo(columnId);
                if (!userId.equals(existing.getAuthor())) {
                    return ResVo.fail(StatusEnum.FORBID_ERROR, "您没有权限编辑此专栏");
                }
                
                columnService.saveColumn(columnDTO);
                log.info("更新专栏成功: userId={}, columnId={}", userId, columnId);
                return ResVo.ok(columnId);
            } else {
                // 创建新专栏
                columnDTO.setAuthor(userId);
                columnService.saveColumn(columnDTO);
                
                // 获取刚创建的专栏ID
                var myColumns = columnService.queryMyColumns(userId);
                if (!myColumns.isEmpty()) {
                    Long newColumnId = myColumns.get(0).getColumnId();
                    log.info("创建专栏成功: userId={}, columnId={}", userId, newColumnId);
                    return ResVo.ok(newColumnId);
                } else {
                    log.error("创建专栏后无法获取ID");
                    return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "创建失败，请重试");
                }
            }
        } catch (Exception e) {
            log.error("保存专栏失败", e);
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除专栏
     * DELETE /column/api/{columnId}
     *
     * @param columnId 专栏ID
     * @return 操作结果
     */
    @OperationLog(value = "删除专栏", module = "专栏管理")
    @DeleteMapping("/{columnId}")
    public ResVo<Void> delete(@PathVariable Long columnId) {
        try {
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            
            // 验证权限：只有作者本人才能删除
            ColumnDTO column = columnService.queryColumnInfo(columnId);
            if (!userId.equals(column.getAuthor())) {
                return ResVo.fail(StatusEnum.FORBID_ERROR, "您没有权限删除此专栏");
            }
            
            columnService.deleteColumn(columnId);
            log.info("删除专栏成功: userId={}, columnId={}", userId, columnId);
            return ResVo.ok(null);
        } catch (Exception e) {
            log.error("删除专栏失败", e);
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResVo<List<ColumnDTO>> search(@RequestParam String key) {
        try {
            List<ColumnDTO> result = columnService.searchColumns(key);
            return ResVo.ok(result);
        } catch (Exception e) {
            log.error("搜索专栏失败", e);
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "搜索失败");
        }
    }

    @GetMapping("/my-articles")
    public ResVo<List<Article>> myArticles() {
        try {
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            List<Article> articles = columnService.getMyArticles(userId);
            return ResVo.ok(articles);
        } catch (Exception e) {
            log.error("获取用户文章失败", e);
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "获取文章失败");
        }
    }

    @PostMapping("/{columnId}/add-article")
    public ResVo<Void> addArticle(@PathVariable Long columnId, @RequestBody Map<String, Object> params) {
        try {
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            ColumnDTO column = columnService.queryColumnInfo(columnId);
            if (!userId.equals(column.getAuthor())) {
                return ResVo.fail(StatusEnum.FORBID_ERROR, "您没有权限操作此专栏");
            }
            Long articleId = Long.parseLong(params.get("articleId").toString());
            columnService.addArticleToColumn(columnId, articleId);
            log.info("添加文章到专栏成功 columnId={}, articleId={}", columnId, articleId);
            return ResVo.ok(null);
        } catch (Exception e) {
            log.error("添加文章到专栏失败", e);
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "添加失败: " + e.getMessage());
        }
    }

    @PostMapping("/{columnId}/remove-article")
    public ResVo<Void> removeArticle(@PathVariable Long columnId, @RequestBody Map<String, Object> params) {
        try {
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            ColumnDTO column = columnService.queryColumnInfo(columnId);
            if (!userId.equals(column.getAuthor())) {
                return ResVo.fail(StatusEnum.FORBID_ERROR, "您没有权限操作此专栏");
            }
            Long articleId = Long.parseLong(params.get("articleId").toString());
            columnService.removeArticleFromColumn(columnId, articleId);
            log.info("从专栏移除文章成功, columnId={}, articleId={}", columnId, articleId);
            return ResVo.ok(null);
        } catch (Exception e) {
            log.error("移除文章失败", e);
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "移除失败: " + e.getMessage());
        }
    }
}


