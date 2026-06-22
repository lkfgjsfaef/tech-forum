package com.example.forum.web.front.article.view;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.enums.ArticleReadTypeEnum;
import com.example.forum.api.model.enums.DocumentTypeEnum;
import com.example.forum.api.model.enums.column.ColumnStatusEnum;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.ArticleOtherDTO;
import com.example.forum.api.model.vo.article.dto.CategoryDTO;
import com.example.forum.api.model.vo.article.dto.ColumnDTO;
import com.example.forum.api.model.vo.article.dto.PayConfirmDTO;
import com.example.forum.api.model.vo.comment.dto.TopCommentDTO;
import com.example.forum.api.model.vo.recommend.SideBarDTO;
import com.example.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.core.util.MarkdownConverter;
import com.example.forum.core.util.SpringUtil;
import com.example.forum.service.article.conveter.PayConverter;
import com.example.forum.service.article.repository.entity.ColumnArticleDO;
import com.example.forum.service.article.service.ArticlePayService;
import com.example.forum.service.article.service.ArticleReadService;
import com.example.forum.service.article.service.ArticleCollectService;
import com.example.forum.service.article.service.ArticleService;
import com.example.forum.service.article.service.CategoryService;
import com.example.forum.service.article.service.ColumnService;
import com.example.forum.service.article.service.TagService;
import com.example.forum.service.comment.service.CommentReadService;
import com.example.forum.service.sidebar.service.SidebarService;
import com.example.forum.service.user.entity.UserFootDO;
import com.example.forum.service.user.mapper.UserFootMapper;
import com.example.forum.service.user.service.UserRelationService;
import com.example.forum.service.user.service.UserService;
import com.example.forum.web.front.article.extra.ArticleReadViewServiceExtend;
import com.example.forum.web.front.article.vo.ArticleDetailVo;
import com.example.forum.web.front.article.vo.ArticleEditVo;
import com.example.forum.web.global.BaseViewController;
import com.example.forum.web.global.SeoInjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 文章
 * todo: 所有的入口都放在一个Controller，会导致功能划分非常混乱
 * 册"文章列表
 * 册"文章编辑
 * 册"文章详情
 * ---
 *  - 返回视图 view
 *  - 返回json数据
 *
 * @author dev
 */
@Controller
@RequestMapping(path = "article")
public class ArticleViewController extends BaseViewController {
    @Autowired
    private ArticleReadService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentReadService commentService;

    @Autowired
    private SidebarService sidebarService;

    @Autowired
    private ColumnService columnService;

    @Autowired
    private ArticleReadViewServiceExtend articleReadViewServiceExtend;

    @Autowired
    private ArticlePayService articlePayService;

    @Autowired
    private ArticleService articleWriteService;

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserFootMapper userFootMapper;

    @Autowired
    private ArticleCollectService articleCollectService;

    /**
     * 文章编辑册"
     *
     * @param articleId
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "edit")
    public String edit(@RequestParam(required = false) Long articleId, Model model) {
        ArticleEditVo vo = new ArticleEditVo();
        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        
        if (articleId != null) {
            ArticleDTO article = articleService.queryDetailArticleInfo(articleId);
            vo.setArticle(article);
            if (!Objects.equals(article.getAuthor(), loginUserId)) {
                // 没有权限
                model.addAttribute("toast", "内容不存在");
                return "redirect:403";
            }

            List<CategoryDTO> categoryList = categoryService.loadAllCategories();
            categoryList.forEach(s -> {
                s.setSelected(s.getCategoryId().equals(article.getCategory().getCategoryId()));
            });
            vo.setCategories(categoryList);
            vo.setTags(article.getTags());
            
            // 加载用户的专栏列册"
            List<ColumnDTO> myColumns = columnService.queryMyColumns(loginUserId);
            vo.setColumns(myColumns);
        } else {
            List<CategoryDTO> categoryList = categoryService.loadAllCategories();
            vo.setCategories(categoryList);
            vo.setTags(Collections.emptyList());
            
            // 加载用户的专栏列册"
            List<ColumnDTO> myColumns = columnService.queryMyColumns(loginUserId);
            vo.setColumns(myColumns);
        }
        model.addAttribute("vo", vo);
        return "views/article-edit/index";
    }


    /**
     * 新的文章详情页URL (SEO优化版本)
     * URL格式: /article/detail/{articleId}/{urlSlug}
     * 示例: /article/detail/123/spring-boot-tutorial
     *
     * @param articleId 文章ID
     * @param urlSlug   URL友好的文章标册"
     * @param model     视图模型
     * @param response  HTTP响应,用于设置301状态码
     * @return 视图名称或重定向URL
     */
    @GetMapping("detail/{articleId}/{urlSlug}")
    public String detailWithSlug(@PathVariable(name = "articleId") Long articleId,
                                 @PathVariable(name = "urlSlug") String urlSlug,
                                 Model model,
                                 HttpServletResponse response) throws IOException {
        // 针对专栏文章，做一个重定向
        ColumnArticleDO columnArticle = columnService.getColumnArticleRelation(articleId);
        if (columnArticle != null) {
            return String.format("redirect:/column/%d/%d", columnArticle.getColumnId(), columnArticle.getSection());
        }

        // 获取文章基本信息以验证slug
        ArticleDTO articleDTO = articleService.queryFullArticleInfo(articleId, ReqInfoContext.getReqInfo().getUserId());

        // 检查slug是否正确,如果不正确则301永久重定向到正确的URL
        if (StringUtils.isNotBlank(articleDTO.getUrlSlug()) && !articleDTO.getUrlSlug().equals(urlSlug)) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            return "redirect:/article/detail/" + articleId + "/" + articleDTO.getUrlSlug();
        }

        // 构建详情页视册"
        return buildDetailView(articleId, model);
    }

    /**
     * 旧的文章详情页URL (仅ID版本,兼容性保册"
     * URL格式: /article/detail/{articleId}
     * 直接显示内容,不重定向(保持向后兼容)
     *
     * @param articleId 文章ID
     * @param model     视图模型
     * @param response  HTTP响应
     * @return 视图名称
     */
    @GetMapping("detail/{articleId}")
    public String detail(@PathVariable(name = "articleId") Long articleId,
                        Model model,
                        HttpServletResponse response) throws IOException {
        // 针对专栏文章，做一个重定向
        ColumnArticleDO columnArticle = columnService.getColumnArticleRelation(articleId);
        if (columnArticle != null) {
            // 检查教程发布状册"
            ColumnDTO column = columnService.queryBasicColumnInfo(columnArticle.getColumnId());
            Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
            
            // 教程未发册"&& 不是作者本册"册"拒绝访问
            if (column.getState() == ColumnStatusEnum.OFFLINE.getCode()) {
                if (loginUserId == null || !loginUserId.equals(column.getAuthor())) {
                    model.addAttribute("toast", "教程未发布，暂时无法访问");
                    return "views/error/403";
                }
                // 作者本人可以预览未发布的教册"
            }
            
            return String.format("redirect:/column/%d/%d", columnArticle.getColumnId(), columnArticle.getSection());
        }

        // 直接显示内容,不重定向(保持向后兼容,避免影响已有的SEO)
        return buildDetailView(articleId, model);
    }

    /**
     * 构建文章详情页视册"
     * 提取公共逻辑,避免代码重复
     *
     * @param articleId 文章ID
     * @param model     视图模型
     * @return 视图名称
     */
    private String buildDetailView(Long articleId, Model model) throws IOException {
        ArticleDetailVo vo = new ArticleDetailVo();
        // 文章相关信息
        ArticleDTO articleDTO = articleService.queryFullArticleInfo(articleId, ReqInfoContext.getReqInfo().getUserId());
        
        // 文章不存在时返回404
        if (articleDTO == null) {
            model.addAttribute("toast", "文章不存在或已被删除");
            return "views/error/404";
        }

        // 更新文章阅读量（每次访问+1册"
        try { articleWriteService.incrementViewCount(articleId); } catch (Exception ignored) {}
        
        // 根据文章类型，来自动处理文章内容
        String content = articleReadViewServiceExtend.formatArticleReadType(articleDTO);
        // 返回给前端页面时，转换为html格式
        articleDTO.setContent(MarkdownConverter.markdownToHtml(content));
        vo.setArticle(articleDTO);

        // 评论信息
        List<TopCommentDTO> comments = commentService.getArticleComments(articleId, PageParam.newPageInstance(1L, 10L));
        vo.setComments(comments);
        vo.setTopCommentTotal(commentService.queryTopCommentCount(articleId));

        // 热门评论
        TopCommentDTO hotComment = commentService.queryHotComment(articleId);
        vo.setHotComment(hotComment);

        // 查询文章的划线评论，用于高亮显示
        List<TopCommentDTO> highlightComments = commentService.queryHighlightComments(articleId);
        vo.setHighlightComments(highlightComments);

        // 作者信息"
        UserStatisticInfoDTO user = userService.queryUserInfoWithStatistic(articleDTO.getAuthorId());
        if (user == null) {
            user = new UserStatisticInfoDTO();
            user.setUserId(articleDTO.getAuthorId());
            user.setUserName(articleDTO.getAuthorName() != null ? articleDTO.getAuthorName() : "未知作册");
            user.setPhoto(null);
            user.setArticleCount(0);
            user.setFollowCount(0);
            user.setFansCount(0);
            user.setFollowerCount(0);
            user.setLikeCount(0);
            user.setCollectCount(0);
        }
        articleDTO.setAuthorName(user.getUserName());
        articleDTO.setAuthorAvatar(user.getPhoto());
        if (articleDTO.getReadType().equals(ArticleReadTypeEnum.PAY_READ.getType())) {
            // 付费阅读的文章，构建收款码信息"
            user.setPayQrCodes(PayConverter.formatPayCodeInfo(user.getPayCode()));
        }
        vo.setAuthor(user);

        // 设置当前登录用户是否已关注作册"
        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        if (loginUserId != null && !loginUserId.equals(articleDTO.getAuthorId())) {
            user.setFollowed(userRelationService.isFollowed(loginUserId, articleDTO.getAuthorId()));
        } else {
            user.setFollowed(false);
        }

        // 设置当前登录用户是否已点赞该文章
        if (loginUserId != null) {
            UserFootDO foot = userFootMapper.findByUserIdAndDocument(loginUserId, articleId, DocumentTypeEnum.ARTICLE.ordinal() + 1);
            articleDTO.setLiked(foot != null && foot.getPraiseStat() != null && foot.getPraiseStat() == 1);
        } else {
            articleDTO.setLiked(false);
        }

        // 设置收藏数和当前用户收藏状册"
        articleDTO.setCollectCount(articleCollectService.getCollectCount(articleId));
        if (loginUserId != null) {
            articleDTO.setCollected(articleCollectService.isCollected(loginUserId, articleId));
        } else {
            articleDTO.setCollected(false);
        }

        // 其他信息封装
        ArticleOtherDTO other = new ArticleOtherDTO();
        other.setReadType(articleDTO.getReadType());
        vo.setOther(other);

        // 打赏用户列表
        if (Objects.equals(articleDTO.getReadType(), ArticleReadTypeEnum.PAY_READ.getType())) {
            vo.setPayUsers(articlePayService.queryPayUsers(articleId));
        } else {
            vo.setPayUsers(Collections.emptyList());
        }

        // 详情页的侧边推荐信息
        List<SideBarDTO> sideBars = sidebarService.queryArticleDetailSidebarList(articleDTO.getAuthorId(), articleDTO.getArticleId());
        vo.setSideBarItems(sideBars);
        model.addAttribute("vo", vo);

        SpringUtil.getBean(SeoInjectService.class).initColumnSeo(vo);
        return "views/article-detail/index";
    }


    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "payConfirm")
    public String payConfirm(@RequestParam("payId") Long payId, Model model) {
        PayConfirmDTO confirmDTO = articlePayService.buildPayConfirmInfo(payId, null);
        if (!ReqInfoContext.getReqInfo().getUserId().equals(confirmDTO.getReceiveUserId())) {
            return "redirect:/error/403";
        }
        model.addAttribute("vo", confirmDTO);
        return "PayConfirm";
    }
}


