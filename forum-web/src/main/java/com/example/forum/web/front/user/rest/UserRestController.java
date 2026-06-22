package com.example.forum.web.front.user.rest;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.enums.FollowTypeEnum;
import com.example.forum.api.model.enums.HomeSelectEnum;
import com.example.forum.api.model.vo.NextPageHtmlVo;
import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.constants.StatusEnum;
import com.example.forum.api.model.vo.user.UserInfoSaveReq;
import com.example.forum.api.model.vo.user.UserRelationReq;
import com.example.forum.api.model.vo.user.dto.FollowUserInfoDTO;
import com.example.forum.core.aop.OperationLog;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.article.service.ArticleReadService;
import com.example.forum.service.user.service.impl.UserRelationServiceImpl;
import com.example.forum.service.user.service.impl.UserServiceImpl;
import com.example.forum.web.component.TemplateEngineHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @author dev
 * @date 2022/9/2
 */
@RestController
@RequestMapping(path = "user/api")
public class UserRestController {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private UserRelationServiceImpl userRelationService;

    @Resource
    private TemplateEngineHelper templateEngineHelper;


    @Resource
    private ArticleReadService articleReadService;


    /**
     * 保存用户关系
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "saveUserRelation")
    public ResVo<Boolean> saveUserRelation(@RequestBody UserRelationReq req) {
        req.setUserId(ReqInfoContext.getReqInfo().getUserId());
        userRelationService.saveUserRelation(req);
        return ResVo.ok(true);
    }

    /**
     * 保存用户详情
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "saveUserInfo")
    @Transactional(rollbackFor = Exception.class)
    public ResVo<Boolean> saveUserInfo(@RequestBody UserInfoSaveReq req) {
        if (req.getUserId() == null || !Objects.equals(req.getUserId(), ReqInfoContext.getReqInfo().getUserId())) {
            // 不能修改其他用户的信息"
            return ResVo.fail(StatusEnum.FORBID_ERROR_MIXED, "无权修改");
        }
        userService.saveUserInfo(req);
        return ResVo.ok(true);
    }

    /**
     * 用户的文章列表翻册"
     *
     * @param userId
     * @param homeSelectType
     * @return
     */
    @GetMapping(path = "articleList")
    public ResVo<NextPageHtmlVo> articleList(@RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "homeSelectType") String homeSelectType,
                                             @RequestParam("page") Long page,
                                             @RequestParam(name = "pageSize", required = false) Long pageSize) {
        HomeSelectEnum select = HomeSelectEnum.fromCode(homeSelectType);
        if (select == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }

        if (pageSize == null) pageSize = PageParam.DEFAULT_PAGE_SIZE;
        PageParam pageParam = PageParam.newPageInstance(page, pageSize);
        PageListVo<ArticleDTO> dto = articleReadService.queryArticlesByUserAndType(userId, pageParam, select);
        String html = templateEngineHelper.renderToVo("views/user/articles/index", "homeSelectList", dto);
        return ResVo.ok(new NextPageHtmlVo(html, dto.getHasMore()));
    }

    @GetMapping(path = "followList")
    public ResVo<NextPageHtmlVo> followList(@RequestParam(name = "userId") Long userId,
                                            @RequestParam(name = "followSelectType") String followSelectType,
                                            @RequestParam("page") Long page,
                                            @RequestParam(name = "pageSize", required = false) Long pageSize) {
        if (pageSize == null) pageSize = PageParam.DEFAULT_PAGE_SIZE;
        PageParam pageParam = PageParam.newPageInstance(page, pageSize);
        PageListVo<FollowUserInfoDTO> followList;
        boolean needUpdateRelation = false;
        if (followSelectType.equals(FollowTypeEnum.FOLLOW.getCode())) {
            followList = userRelationService.getUserFollowList(userId, pageParam);
        } else {
            // 查询粉丝列表时，只能确定粉丝关注了userId，但是不能反向判断，因此需要再更新下映射关系，判断userId是否有关注这个用册"
            followList = userRelationService.getUserFansList(userId, pageParam);
            needUpdateRelation = true;
        }

        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        if (!Objects.equals(loginUserId, userId) || needUpdateRelation) {
            userRelationService.updateUserFollowRelationId(followList, userId);
        }
        String html = templateEngineHelper.renderToVo("views/user/follows/index", "followList", followList);
        return ResVo.ok(new NextPageHtmlVo(html, followList.getHasMore()));
    }

    @Permission(role = UserRole.LOGIN)
    @OperationLog(value = "修改密码", module = "用户安全")
    @PostMapping(path = "changePassword")
    public ResVo<Boolean> changePassword(@RequestBody Map<String, String> params) {
        try {
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");
            if (oldPassword == null || oldPassword.isEmpty()) {
                return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "请输入原密码");
            }
            if (newPassword == null || newPassword.length() < 6) {
                return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "新密码长度不能少册");
            }
            userService.changePassword(userId, oldPassword, newPassword);
            return ResVo.ok(true);
        } catch (Exception e) {
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR, e.getMessage());
        }
    }
}

