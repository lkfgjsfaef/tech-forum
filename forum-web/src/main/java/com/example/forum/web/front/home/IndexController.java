package com.example.forum.web.front.home;

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
import com.example.forum.web.front.home.helper.IndexRecommendHelper;
import com.example.forum.web.front.home.vo.IndexVo;
import com.example.forum.web.global.BaseViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author dev
 * @date 2022/7/6
 */
@Controller
public class IndexController extends BaseViewController {
    @Autowired
    private IndexRecommendHelper indexRecommendHelper;

    @Autowired
    private UserActivityRankService userActivityRankService;
    
    @Autowired
    private ArticleReadService articleService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private TagService tagService;

    @GetMapping(path = {"/", "", "/index"})
    public String index(Model model, HttpServletRequest request,
                       @RequestParam(value = "page", defaultValue = "1") Long page,
                       @RequestParam(value = "category", required = false) String category,
                       @RequestParam(value = "tag", required = false) String tag,
                       @RequestParam(value = "rankType", defaultValue = "month") String rankType) {
        
        IndexVo vo;
        String currentCategory = "";
        
        // 优先按标签筛册"
        if (tag != null && !tag.isEmpty()) {
            Long tagId = tagService.loadAllTags().stream()
                    .filter(t -> tag.equals(t.getTag()))
                    .map(TagDTO::getTagId)
                    .findFirst().orElse(null);
            if (tagId != null) {
                vo = indexRecommendHelper.buildIndexVo(null, page);
                PageListVo<ArticleDTO> articles = articleService.queryArticlesByTag(tagId, PageParam.newPageInstance(page, 10L));
                vo.setArticles(articles);
                vo.setCurrentCategory(tag);
            } else {
                vo = indexRecommendHelper.buildIndexVo(null, page);
            }
        }
        // 按分类筛册"
        else if (category != null && !category.isEmpty()) {
            Long categoryId = categoryService.loadAllCategories().stream()
                    .filter(c -> category.equals(c.getCategory()))
                    .map(CategoryDTO::getCategoryId)
                    .findFirst().orElse(null);
            if (categoryId != null) {
                vo = indexRecommendHelper.buildIndexVo(null, page);
                PageListVo<ArticleDTO> articles = articleService.queryArticlesByCategory(categoryId, PageParam.newPageInstance(page, 10L));
                vo.setArticles(articles);
                vo.setCurrentCategory(category);
            } else {
                vo = indexRecommendHelper.buildIndexVo(null, page);
            }
        }
        // 默认首页
        else {
            String requestCategory = request.getParameter("category");
            if (requestCategory != null && !requestCategory.isEmpty()) {
                final String filterCategory = requestCategory;
                Long categoryId = categoryService.loadAllCategories().stream()
                        .filter(c -> filterCategory.equals(c.getCategory()))
                        .map(CategoryDTO::getCategoryId)
                        .findFirst().orElse(null);
                vo = indexRecommendHelper.buildIndexVo(null, page);
                if (categoryId != null) {
                    PageListVo<ArticleDTO> articles = articleService.queryArticlesByCategory(categoryId, PageParam.newPageInstance(page, 10L));
                    vo.setArticles(articles);
                    vo.setCurrentCategory(filterCategory);
                }
            } else {
                vo = indexRecommendHelper.buildIndexVo(null, page);
            }
        }
        
        model.addAttribute("vo", vo);
        
        // 根据排行榜类型参数查询排行数册"
        ActivityRankTimeEnum rankTimeEnum = ActivityRankTimeEnum.nameOf(rankType);
        if (rankTimeEnum == null) {
            rankTimeEnum = ActivityRankTimeEnum.MONTH;
        }
        List<RankItemDTO> rankList = userActivityRankService.queryRankList(rankTimeEnum, 10);
        if (rankList != null) {
            model.addAttribute("rankItems", rankList);
        }
        model.addAttribute("rankType", rankType);
        
        return "views/home/index";
    }
}

