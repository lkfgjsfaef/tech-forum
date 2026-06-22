package com.example.forum.web.front.search.view;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.CategoryDTO;
import com.example.forum.api.model.vo.article.dto.TagDTO;
import com.example.forum.service.article.service.ArticleReadService;
import com.example.forum.service.article.service.CategoryService;
import com.example.forum.service.article.service.TagService;
import com.example.forum.web.front.home.helper.IndexRecommendHelper;
import com.example.forum.web.front.home.vo.IndexVo;
import com.example.forum.web.front.article.vo.ArticleListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 搜索服务接口
 *
 * @author dev
 * @date 2022/10/28
 */
@Controller
public class SearchViewController {
    @Autowired
    private IndexRecommendHelper indexRecommendHelper;
    @Autowired
    private ArticleReadService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;


    /**
     * 搜索文章列表
     */
    @GetMapping(path = "search")
    public String searchArticleList(
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", defaultValue = "1") Long page,
            Model model) {
        
        ArticleListVo vo = new ArticleListVo();
        model.addAttribute("key", key);
        model.addAttribute("category", category);
        
        if (!StringUtils.isBlank(key)) {
            // 先执行搜索，获取搜索结果
            PageParam pageParam = PageParam.newPageInstance(page, 15L);
            PageListVo<ArticleDTO> searchedArticles = articleService.queryArticlesBySearchKey(key, pageParam);
            
            // 如果选择了分册"标签，在搜索结果内进一步筛册"
            if (!StringUtils.isBlank(category)) {
                // 先尝试按分类名匹册"
                Long categoryId = categoryService.loadAllCategories().stream()
                        .filter(c -> category.equals(c.getCategory()))
                        .map(CategoryDTO::getCategoryId)
                        .findFirst().orElse(null);
                
                if (categoryId != null) {
                    // 按分类ID过滤搜索结果
                    List<ArticleDTO> filtered = searchedArticles.getList().stream()
                            .filter(a -> a.getCategory() != null && categoryId.equals(a.getCategory().getCategoryId()))
                            .collect(java.util.stream.Collectors.toList());
                    long total = filtered.size();
                    int fromIndex = (int) Math.min(pageParam.getOffset(), total);
                    int toIndex = (int) Math.min(pageParam.getOffset() + pageParam.getPageSize(), total);
                    List<ArticleDTO> pagedList = filtered.subList(fromIndex, toIndex);
                    vo.setArticles(PageListVo.of(pagedList, total, pageParam.getPageNum(), pageParam.getPageSize()));
                } else {
                    vo.setArticles(searchedArticles);
                }
            } else {
                vo.setArticles(searchedArticles);
            }
            model.addAttribute("vo", vo);
        }
        
        return "views/article-search-list/index";
    }

}

