package com.example.forum.web.front.article.view;

import com.example.forum.api.model.enums.rank.ActivityRankTimeEnum;
import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.CategoryDTO;
import com.example.forum.api.model.vo.article.dto.TagDTO;
import com.example.forum.api.model.vo.rank.dto.RankItemDTO;
import com.example.forum.service.article.service.ArticleReadService;
import com.example.forum.service.article.service.CategoryService;
import com.example.forum.service.article.service.TagService;
import com.example.forum.service.rank.service.UserActivityRankService;
import com.example.forum.web.front.article.vo.ArticleListVo;
import com.example.forum.web.global.BaseViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章列表视图
 *
 * @author dev
 */
@RequestMapping(path = "article")
@Controller
public class ArticleListViewController extends BaseViewController {
    @Autowired
    private ArticleReadService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserActivityRankService userActivityRankService;

    /**
     * 全部文章列表页面（支持分类、标签、搜索、时间筛选）
     */
    @GetMapping(path = "/list")
    public String articleList(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "time", required = false) Integer time,
            @RequestParam(value = "page", defaultValue = "1") Long page,
            Model model) {
        
        ArticleListVo vo = new ArticleListVo();
        PageParam pageParam = PageParam.newPageInstance(page, 15L);
        PageListVo<ArticleDTO> articles;
        String archiveName = "";
        String currentCategory = "";
        
        // 优先按标签筛选
        if (tag != null && !tag.isEmpty()) {
            Long tagId = tagService.loadAllTags().stream()
                    .filter(t -> tag.equals(t.getTag()))
                    .map(TagDTO::getTagId)
                    .findFirst().orElse(null);
            articles = tagId != null ? articleService.queryArticlesByTag(tagId, pageParam) : PageListVo.emptyVo();
            archiveName = tag;
            currentCategory = "";
            vo.setCurrentTag(tag);
        } 
        // 按分类筛选
        else if (category != null && !category.isEmpty()) {
            Long categoryId = categoryService.loadAllCategories().stream()
                    .filter(c -> category.equals(c.getCategory()))
                    .map(CategoryDTO::getCategoryId)
                    .findFirst().orElse(null);
            articles = categoryId != null ? articleService.queryArticlesByCategory(categoryId, pageParam) : PageListVo.emptyVo();
            archiveName = category;
            currentCategory = category;
        }
        // 按时间筛选
        else if (time != null && time > 0) {
            articles = articleService.queryArticlesByTime(time, pageParam);
            archiveName = getTimeFilterName(time);
            currentCategory = "";
        }
        // 全部文章
        else {
            articles = articleService.queryArticles(pageParam);
            archiveName = "全部文章";
            currentCategory = "";
        }
        
        vo.setArticles(articles);
        vo.setArchives(archiveName);
        vo.setCurrentCategory(currentCategory);
        model.addAttribute("vo", vo);
        
        // 添加时间筛选信息到model
        model.addAttribute("timeFilter", time);
        
        // 计算分页页码范围
        if (articles != null && articles.getTotalPages() != null && articles.getTotalPages() > 0) {
            int currentPage = articles.getPageNum();
            int totalPages = articles.getTotalPages();
            List<Integer> pageNumbers = new ArrayList<>();
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(totalPages, currentPage + 2);
            // 确保至少显示5个页码（如果总页数够的话）
            if (end - start < 4 && totalPages >= 5) {
                if (start == 1) {
                    end = Math.min(totalPages, start + 4);
                } else if (end == totalPages) {
                    start = Math.max(1, end - 4);
                }
            }
            for (int i = start; i <= end; i++) {
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("showFirstPage", start > 1);
            model.addAttribute("showLastPage", end < totalPages);
        } else {
            // 没有分页数据时也设置默认值，避免模板NPE
            model.addAttribute("pageNumbers", new ArrayList<Integer>());
            model.addAttribute("showFirstPage", false);
            model.addAttribute("showLastPage", false);
        }
        
        // 添加排行榜数据
        List<RankItemDTO> rankList = userActivityRankService.queryRankList(ActivityRankTimeEnum.MONTH, 10);
        if (rankList != null) {
            model.addAttribute("rankItems", rankList);
        }
        
        return "views/article-list/index";
    }

    /**
     * 根据时间筛选值获取名称
     */
    private String getTimeFilterName(Integer time) {
        if (time == null) return "全部文章";
        switch (time) {
            case 1: return "今日热文";
            case 7: return "最近7天";
            case 30: return "最近30天";
            case 90: return "最近3个月";
            case 365: return "最近一年";
            default: return "全部文章";
        }
    }

    /**
     * 查询某个分类下的文章列表
     */
    @GetMapping(path = "category/{category}")
    public String categoryList(@PathVariable("category") String category, 
                              @RequestParam(value = "page", defaultValue = "1") Long page,
                              Model model) {
        return articleList(category, null, null, page, model);
    }

    /**
     * 查询某个标签下文章列表
     */
    @GetMapping(path = "tag/{tag}")
    public String tagList(@PathVariable("tag") String tag,
                         @RequestParam(value = "page", defaultValue = "1") Long page,
                         Model model) {
        return articleList(null, tag, null, page, model);
    }
}

