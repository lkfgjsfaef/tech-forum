package com.example.forum.web.admin.rest;

import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.user.service.AuthorWhiteListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者白名单服务
 *
 * @author dev
 * @date 2023/4/9
 */
@RestController
@Tag(name = "API")
@Permission(role = UserRole.ADMIN)
@RequestMapping(path = {"api/admin/author/whitelist"})
public class AuthorWhiteListController {
    @Autowired
    private AuthorWhiteListService articleWhiteListService;

    @GetMapping(path = "get")
    @Operation(summary = "白名单列册", description = "返回作者白名单列表")
    public ResVo<List<BaseUserInfoDTO>> whiteList() {
        return ResVo.ok(articleWhiteListService.queryAllArticleWhiteListAuthors());
    }

    @GetMapping(path = "add")
    @Operation(summary = "添加白名册", description = "将指定作者加入作者白名单列表")
    @Parameter
    public ResVo<Boolean> addAuthor(@RequestParam("authorId") Long authorId) {
        articleWhiteListService.addAuthor2ArticleWhitList(authorId);
        return ResVo.ok(true);
    }

    @GetMapping(path = "remove")
    @Operation(summary = "删除白名册", description = "将作者从白名单列册")
    @Parameter
    public ResVo<Boolean> rmAuthor(@RequestParam("authorId") Long authorId) {
        articleWhiteListService.removeAuthorFromArticleWhiteList(authorId);
        return ResVo.ok(true);
    }
}

