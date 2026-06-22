package com.example.forum.web.admin.rest;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.enums.OperateArticleEnum;
import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.article.AiSeoGenerateReq;
import com.example.forum.api.model.vo.article.AiSeoGenerateRes;
import com.example.forum.api.model.vo.article.AiSlugGenerateReq;
import com.example.forum.api.model.vo.article.ArticlePostReq;
import com.example.forum.api.model.vo.article.SearchArticleReq;
import com.example.forum.api.model.vo.article.dto.ArticleAdminDTO;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.SimpleArticleDTO;
import com.example.forum.api.model.vo.constants.StatusEnum;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.core.util.NumUtil;
import com.example.forum.service.article.service.AiSeoService;
import com.example.forum.service.article.service.ArticleReadService;
import com.example.forum.service.article.service.ArticleSettingService;
import com.example.forum.service.article.service.ArticleWriteService;
import com.example.forum.web.front.search.vo.SearchArticleVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章后台
 *
 * @author dev
 * @date 2022/9/19
 */
@RestController
@Permission(role = UserRole.LOGIN)
@Tag(name = "API")
@RequestMapping(path = {"/api/admin/article/", "/admin/article/"})
public class ArticleSettingRestController {

    @Autowired
    private ArticleSettingService articleSettingService;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private ArticleWriteService articleWriteService;

    @Autowired
    private AiSeoService aiSeoService;

    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody ArticlePostReq req) {
        if (NumUtil.nullOrZero(req.getId(), true)) {
            this.articleWriteService.saveArticle(req, ReqInfoContext.getReqInfo().getUserId());
        } else {
            this.articleWriteService.saveArticle(req, null);
        }
        return ResVo.ok();
    }

    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "update")
    public ResVo<String> update(@RequestBody ArticlePostReq req) {
        articleSettingService.updateArticle(req);
        return ResVo.ok();
    }

    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResVo<String> operate(@RequestParam(name = "articleId") Long articleId, @RequestParam(name = "operateType") Integer operateType) {
        OperateArticleEnum operate = OperateArticleEnum.fromCode(operateType);
        if (operate == OperateArticleEnum.EMPTY) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, operateType + "非法");
        }
        articleSettingService.operateArticle(articleId, operate);
        return ResVo.ok();
    }


    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResVo<String> delete(@RequestParam(name = "articleId") Long articleId) {
        articleSettingService.deleteArticle(articleId);
        return ResVo.ok();
    }

    @Operation(summary = "根据文章id获取文章详情")
    @GetMapping(path = "detail")
    public ResVo<ArticleDTO> detail(@RequestParam(name = "articleId", required = false) Long articleId) {
        ArticleDTO articleDTO = new ArticleDTO();
        if (articleId != null) {
            articleDTO = articleReadService.queryDetailArticleInfo(articleId);
        }

        return ResVo.ok(articleDTO);
    }

    @Operation(summary = "获取文章列表")
    @PostMapping(path = "list")
    public ResVo<PageVo<ArticleAdminDTO>> list(@RequestBody SearchArticleReq req) {
        PageVo<ArticleAdminDTO> articleDTOPageVo = articleSettingService.getArticleList(req);
        return ResVo.ok(articleDTOPageVo);
    }

    @Operation(summary = "文章搜索，按照文章标题关键字")
    @GetMapping(path = "query")
    public ResVo<SearchArticleVo> queryArticleList(@RequestParam(name = "key", required = false) String key) {
        List<SimpleArticleDTO> list = articleReadService.querySimpleArticleBySearchKey(key);
        SearchArticleVo vo = new SearchArticleVo();
        vo.setKey(key);
        vo.setItems(list);
        return ResVo.ok(vo);
    }

    @Operation(summary = "AI生成SEO标题和描述")
    @PostMapping(path = "generate/seo")
    public ResVo<AiSeoGenerateRes> generateSeo(@RequestBody AiSeoGenerateReq req) {
        AiSeoGenerateRes response = aiSeoService.generateSeoTitleAndDescription(req);
        return ResVo.ok(response);
    }

    @Operation(summary = "生成文章语义URL")
    @PostMapping(path = "generate/slug")
    public ResVo<String> generateSlug(@RequestBody AiSlugGenerateReq req) {
        return ResVo.ok(articleSettingService.generateUrlSlug(req));
    }

}
