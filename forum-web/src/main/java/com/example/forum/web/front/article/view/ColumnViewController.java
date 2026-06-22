package com.example.forum.web.front.article.view;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.enums.ArticleReadTypeEnum;
import com.example.forum.api.model.enums.DocumentTypeEnum;
import com.example.forum.api.model.enums.column.ColumnArticleReadEnum;
import com.example.forum.api.model.enums.column.ColumnStatusEnum;
import com.example.forum.api.model.enums.column.ColumnTypeEnum;
import com.example.forum.api.model.enums.user.UserAIStatEnum;
import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.ArticleOtherDTO;
import com.example.forum.api.model.vo.article.dto.ColumnArticleFlipDTO;
import com.example.forum.api.model.vo.article.dto.ColumnArticlesDTO;
import com.example.forum.api.model.vo.article.dto.ColumnDTO;
import com.example.forum.api.model.vo.article.dto.SimpleArticleDTO;
import com.example.forum.api.model.vo.comment.dto.TopCommentDTO;
import com.example.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import com.example.forum.api.model.vo.recommend.SideBarDTO;
import com.example.forum.core.util.MarkdownConverter;
import com.example.forum.core.util.SpringUtil;
import com.example.forum.core.util.StrUtil;
import com.example.forum.service.article.repository.entity.ColumnArticleDO;
import com.example.forum.service.article.service.ArticleCollectService;
import com.example.forum.service.article.service.ArticlePayService;
import com.example.forum.service.article.service.ArticleReadService;
import com.example.forum.service.article.service.ArticleWriteService;
import com.example.forum.service.article.service.ColumnService;
import com.example.forum.service.comment.service.CommentReadService;
import com.example.forum.service.sidebar.service.SidebarService;
import com.example.forum.service.user.entity.UserFootDO;
import com.example.forum.service.user.mapper.UserFootMapper;
import com.example.forum.service.user.service.UserRelationService;
import com.example.forum.service.user.service.UserService;
import com.example.forum.web.config.GlobalViewConfig;
import com.example.forum.web.front.article.vo.ColumnVo;
import com.example.forum.web.global.SeoInjectService;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import liquibase.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 专栏入口
 *
 * @author dev
 * @date 2022/9/15
 */
@Controller
@RequestMapping(path = "column")
public class ColumnViewController {
    @Autowired
    private ColumnService columnService;
    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private SidebarService sidebarService;

    @Resource
    private GlobalViewConfig globalViewConfig;
    @Autowired
    private ArticlePayService articlePayService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private ArticleCollectService articleCollectService;
    @Autowired
    private UserFootMapper userFootMapper;
    @Autowired
    private ArticleWriteService articleWriteService;

    /**
     * 专栏主页，展示专栏列表
     *
     * @param model
     * @return
     */
    @GetMapping(path = {"list", "/", "", "home"})
    public String list(Model model) {
        PageListVo<ColumnDTO> columns = columnService.listColumn(PageParam.newPageInstance(1L, 6L));
        List<SideBarDTO> sidebars = sidebarService.queryColumnSidebarList();
        ColumnVo vo = new ColumnVo();
        vo.setColumns(columns);
        vo.setSideBarItems(sidebars);
        model.addAttribute("vo", vo);
        return "views/column-home/index";
    }

    /**
     * 创建新专栏页面
     *
     * @param model
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "create")
    public String create(Model model) {
        ColumnVo vo = new ColumnVo();
        model.addAttribute("vo", vo);
        return "views/column-edit/index";
    }

    /**
     * 编辑专栏页面
     *
     * @param columnId 专栏ID
     * @param model
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "{columnId}/edit")
    public String edit(@PathVariable("columnId") Long columnId, Model model) {
        // 查询专栏信息
        ColumnDTO column = columnService.queryColumnInfo(columnId);
        
        // 验证权限：只有作者本人才能编辑
        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        if (!loginUserId.equals(column.getAuthor())) {
            model.addAttribute("toast", "您没有权限编辑此专栏");
            return "views/error/403";
        }
        
        ColumnVo vo = new ColumnVo();
        vo.setColumn(column);
        vo.setCover(column.getCover());
        model.addAttribute("vo", vo);
        return "views/column-edit/index";
    }

    /**
     * 专栏详情
     *
     * @param columnId
     * @return
     */
    @GetMapping(path = "{columnId}")
    public String column(@PathVariable("columnId") Long columnId, Model model) {
        ColumnDTO dto = columnService.queryColumnInfo(columnId);
        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        
        // 教程未发布 && 不是作者本人，拒绝访问
        if (dto.getState() == ColumnStatusEnum.OFFLINE.getCode()) {
            if (loginUserId == null || !loginUserId.equals(dto.getAuthor())) {
                model.addAttribute("toast", "教程未发布，暂时无法访问");
                return "views/error/403";
            }
        }
        
        model.addAttribute("vo", dto);
        return "/views/column-index/index";
    }


    /**
     * 专栏的文章阅读界面
     *
     * @param columnId 专栏id
     * @param section  节数，从1开始
     * @param model
     * @return
     */
    @GetMapping(path = "{columnId}/{section}")
    public String articles(@PathVariable("columnId") Long columnId, @PathVariable("section") Integer section, Model model) {
        if (section <= 0) section = 1;
        // 查询专栏
        ColumnDTO column = columnService.queryBasicColumnInfo(columnId);
        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        
        // 教程未发布 && 不是作者本人，拒绝访问
        if (column.getState() == ColumnStatusEnum.OFFLINE.getCode()) {
            if (loginUserId == null || !loginUserId.equals(column.getAuthor())) {
                model.addAttribute("toast", "教程未发布，暂时无法访问");
                return "views/error/403";
            }
        }

        ColumnArticleDO columnArticle = columnService.queryColumnArticle(columnId, section);
        if (columnArticle == null) {
            List<SimpleArticleDTO> allArticles = columnService.queryColumnArticles(columnId);
            if (allArticles == null || allArticles.isEmpty() || section > allArticles.size()) {
                model.addAttribute("toast", "该章节暂无内容");
                return "views/error/404";
            }
            SimpleArticleDTO fallback = allArticles.get(section - 1);
            if (fallback == null || fallback.getArticleId() == null) {
                model.addAttribute("toast", "该章节暂无内册");
                return "views/error/404";
            }
            columnArticle = new ColumnArticleDO();
            columnArticle.setArticleId(fallback.getArticleId());
            columnArticle.setReadType(0);
        }
        Long articleId = columnArticle.getArticleId();
        try { articleWriteService.incrementViewCount(articleId); } catch (Exception ignored) {}
        // 文章信息
        ArticleDTO articleDTO = articleReadService.queryFullArticleInfo(articleId, ReqInfoContext.getReqInfo().getUserId());
        // 返回html格式的文档内册"
        articleDTO.setContent(MarkdownConverter.markdownToHtml(articleDTO.getContent()));
        // 评论信息
        List<TopCommentDTO> comments = commentReadService.getArticleComments(articleId, PageParam.newPageInstance());
        Integer topCommentTotal = commentReadService.queryTopCommentCount(articleId);

        // 热门评论
        TopCommentDTO hotComment = commentReadService.queryHotComment(articleId);

        List<TopCommentDTO> highlightComment = commentReadService.queryHighlightComments(articleId);

        // 文章列表
        List<SimpleArticleDTO> articles = columnService.queryColumnArticles(columnId);

        ColumnArticlesDTO vo = new ColumnArticlesDTO();
        vo.setArticle(articleDTO);
        vo.setComments(comments);
        vo.setTopCommentTotal(topCommentTotal);
        vo.setHotComment(hotComment);
        vo.setHighlightComments(highlightComment);
        ColumnDTO columnDto = columnService.queryBasicColumnInfo(columnId);
        vo.setColumn(columnDto);
        vo.setSection(section);
        vo.setArticleList(articles);

        // 作者信息（册"null 保护，兼容旧数据/已删除用户）
        UserStatisticInfoDTO author = userService.queryUserInfoWithStatistic(articleDTO.getAuthorId());
        if (author == null) {
            author = new UserStatisticInfoDTO();
            author.setUserId(articleDTO.getAuthorId());
            author.setUserName(articleDTO.getAuthorName() != null ? articleDTO.getAuthorName() : "未知作册");
            author.setPhoto(null);
            author.setArticleCount(0);
            author.setFollowCount(0);
            author.setFansCount(0);
            author.setFollowerCount(0);
            author.setLikeCount(0);
            author.setCollectCount(0);
        }
        articleDTO.setAuthorName(author.getUserName());
        articleDTO.setAuthorAvatar(author.getPhoto());
        // 当前用户是否关注作册"
        if (loginUserId != null && !loginUserId.equals(articleDTO.getAuthorId())) {
            author.setFollowed(userRelationService.isFollowed(loginUserId, articleDTO.getAuthorId()));
        } else {
            author.setFollowed(false);
        }
        // 当前用户是否点赞
        if (loginUserId != null) {
            UserFootDO foot = userFootMapper.findByUserIdAndDocument(loginUserId, articleId, DocumentTypeEnum.ARTICLE.ordinal() + 1);
            articleDTO.setLiked(foot != null && foot.getPraiseStat() != null && foot.getPraiseStat() == 1);
        } else {
            articleDTO.setLiked(false);
        }
        // 收藏数和收藏状册"
        articleDTO.setCollectCount(articleCollectService.getCollectCount(articleId));
        if (loginUserId != null) {
            articleDTO.setCollected(articleCollectService.isCollected(loginUserId, articleId));
        } else {
            articleDTO.setCollected(false);
        }
        vo.setAuthor(author);

        ArticleOtherDTO other = new ArticleOtherDTO();

        // 教程类型
        updateReadType(other, column, articleDTO, ColumnArticleReadEnum.valueOf(columnArticle.getReadType()));


        // 把是文章翻页的参数封装到这里
        // prev 册"href 册"是否显示册"flag
        ColumnArticleFlipDTO flip = new ColumnArticleFlipDTO();
        flip.setPrevHref("/column/" + columnId + "/" + (section - 1));
        flip.setPrevShow(section > 1);
        // next 册"href 册"是否显示册"flag
        flip.setNextHref("/column/" + columnId + "/" + (section + 1));
        flip.setNextShow(section < articles.size());
        other.setFlip(flip);

        // 放入 model 册"
        vo.setOther(other);

        // 打赏用户列表
        if (Objects.equals(articleDTO.getReadType(), ArticleReadTypeEnum.PAY_READ.getType())) {
            vo.setPayUsers(articlePayService.queryPayUsers(articleId));
        } else {
            vo.setPayUsers(Collections.emptyList());
        }
        model.addAttribute("vo", vo);

        SpringUtil.getBean(SeoInjectService.class).initColumnSeo(vo, column);
        return "views/column-detail/index";
    }

    /**
     * 对于要求登录阅读的文章进行进行处册"
     *
     * @param vo
     * @param column
     * @param articleDTO
     */
    private void updateReadType(ArticleOtherDTO vo, ColumnDTO column, ArticleDTO articleDTO, ColumnArticleReadEnum articleReadEnum) {
        Long loginUser = ReqInfoContext.getReqInfo().getUserId();
        if (loginUser != null && loginUser.equals(articleDTO.getAuthor())) {
            vo.setReadType(ColumnTypeEnum.FREE.getType());
            return;
        }

        if (articleReadEnum == ColumnArticleReadEnum.COLUMN_TYPE) {
            // 专栏中的文章，没有特殊指定时，直接沿用专栏的规则
            if (column.getType() == ColumnTypeEnum.TIME_FREE.getType()) {
                long now = System.currentTimeMillis();
                if (now > column.getFreeEndTime() || now < column.getFreeStartTime()) {
                    vo.setReadType(ColumnTypeEnum.LOGIN.getType());
                } else {
                    vo.setReadType(ColumnTypeEnum.FREE.getType());
                }
            } else {
                vo.setReadType(column.getType());
            }
        } else {
            // 直接使用文章特殊设置的规册"
            vo.setReadType(articleReadEnum.getRead());
        }
        // 如果是星册"or 登录阅读时，不返回全量的文章内容
        articleDTO.setContent(trimContent(vo.getReadType(), articleDTO.getContent()));
        // fix 关于 cover 封面，文章详情的前端已经不显示了，这里直接删册"
    }

    /**
     * 文章内容隐藏
     *
     * @param readType
     * @param content
     * @return
     */
    private String trimContent(int readType, String content) {
        if (readType == ColumnTypeEnum.STAR_READ.getType()) {
            // 判断登录用户是否绑定了星球，如果是，则直接阅读完整的专栏内容
            if (ReqInfoContext.getReqInfo().getUser() != null && ReqInfoContext.getReqInfo().getUser().getStarStatus() != null && ReqInfoContext.getReqInfo().getUser().getStarStatus().equals(UserAIStatEnum.FORMAL.getCode())) {
                return content;
            }

            // 如果没有绑定星球，则返回 10% 的内册"
            int count = Integer.parseInt(globalViewConfig.getZsxqArticleReadCount());
            return StrUtil.safeSubstringHtml(content, content.length() * count / 100);
        }

        if ((readType == ColumnTypeEnum.LOGIN.getType() && ReqInfoContext.getReqInfo().getUserId() == null)) {
            // 如果是登录阅读，但是用户没有登录，则返回 20% 的内册"
            int count = Integer.parseInt(globalViewConfig.getNeedLoginArticleReadCount());
            return StrUtil.safeSubstringHtml(content, content.length() * count / 100);
        }

        return content;
    }
}

