package com.example.forum.web.front.user.view;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.enums.FollowSelectEnum;
import com.example.forum.api.model.enums.FollowTypeEnum;
import com.example.forum.api.model.enums.HomeSelectEnum;
import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.TagSelectDTO;
import com.example.forum.api.model.vo.user.dto.FollowUserInfoDTO;
import com.example.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.core.util.SpringUtil;
import com.example.forum.service.article.conveter.PayConverter;
import com.example.forum.service.article.service.ArticleReadService;
import com.example.forum.service.user.service.UserRelationService;
import com.example.forum.service.user.service.UserService;
import com.example.forum.web.front.user.vo.UserHomeVo;
import com.example.forum.web.global.BaseViewController;
import com.example.forum.web.global.SeoInjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * 用户注册、取消，登录、登册"
 *
 * @author dev
 * @date : 2022/8/3 10:56
 **/
@Controller
@RequestMapping(path = "user")
@Slf4j
public class UserViewController extends BaseViewController {

    @Resource
    private UserService userService;

    @Resource
    private UserRelationService userRelationService;

    @Resource
    private ArticleReadService articleReadService;

    private static final List<String> homeSelectTags = Arrays.asList("article", "collect", "follow", "fans");
    private static final List<String> followSelectTags = Arrays.asList("follow", "fans");

    /**
     * 获取用户主页信息，通常只有作者本人才能进入这个页册"
     *
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "home")
    public String getUserHome(@RequestParam(name = "userId") Long userId,
                              @RequestParam(name = "homeSelectType", required = false) String homeSelectType,
                              @RequestParam(name = "followSelectType", required = false) String followSelectType,
                              Model model) {
        UserHomeVo vo = new UserHomeVo();
        vo.setHomeSelectType(StringUtils.isBlank(homeSelectType) ? HomeSelectEnum.ARTICLE.getCode() : homeSelectType);
        vo.setFollowSelectType(StringUtils.isBlank(followSelectType) ? FollowTypeEnum.FOLLOW.getCode() : followSelectType);

        UserStatisticInfoDTO userInfo = userService.queryUserInfoWithStatistic(userId);
        vo.setUserHome(userInfo);

        List<TagSelectDTO> homeSelectTags = homeSelectTags(vo.getHomeSelectType(), Objects.equals(userId, ReqInfoContext.getReqInfo().getUserId()));
        vo.setHomeSelectTags(homeSelectTags);

        vo.setPayQrCodes(PayConverter.formatPayCodeInfoAsMap(userInfo.getPayCode()));

        userHomeSelectList(vo, userId);
        model.addAttribute("vo", vo);

        Long loginUserId = ReqInfoContext.getReqInfo() != null ? ReqInfoContext.getReqInfo().getUserId() : null;
        setupPageFlags(model, loginUserId, userId, vo.getHomeSelectType());

        SpringUtil.getBean(SeoInjectService.class).initUserSeo(vo);
        return "views/user/index";
    }

    @GetMapping(path = "/{userId:\\d+}")
    public String detail(@PathVariable(name = "userId") Long userId, @RequestParam(name = "homeSelectType", required = false) String homeSelectType,
                         @RequestParam(name = "followSelectType", required = false) String followSelectType,
                         Model model) {
        UserHomeVo vo = new UserHomeVo();
        vo.setHomeSelectType(StringUtils.isBlank(homeSelectType) ? HomeSelectEnum.ARTICLE.getCode() : homeSelectType);
        vo.setFollowSelectType(StringUtils.isBlank(followSelectType) ? FollowTypeEnum.FOLLOW.getCode() : followSelectType);

        UserStatisticInfoDTO userInfo = userService.queryUserInfoWithStatistic(userId);
        vo.setUserHome(userInfo);

        List<TagSelectDTO> homeSelectTags = homeSelectTags(vo.getHomeSelectType(), Objects.equals(userId, ReqInfoContext.getReqInfo().getUserId()));
        vo.setHomeSelectTags(homeSelectTags);

        userHomeSelectList(vo, userId);
        model.addAttribute("vo", vo);

        Long loginUserId = ReqInfoContext.getReqInfo() != null ? ReqInfoContext.getReqInfo().getUserId() : null;
        setupPageFlags(model, loginUserId, userId, vo.getHomeSelectType());

        SpringUtil.getBean(SeoInjectService.class).initUserSeo(vo);
        return "views/user/index";
    }

    private void setupPageFlags(Model model, Long loginUserId, Long pageUserId, String selectType) {
        boolean isLoggedIn = loginUserId != null;
        boolean isOwner = isLoggedIn && loginUserId.equals(pageUserId);
        boolean isArticleTab = "article".equals(selectType);
        boolean isCollectTab = "collect".equals(selectType);
        boolean isFollowTab = "follow".equals(selectType);
        boolean isFansTab = "fans".equals(selectType);

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isArticleTab", isArticleTab);
        model.addAttribute("isCollectTab", isCollectTab);
        model.addAttribute("isFollowTab", isFollowTab);
        model.addAttribute("isFansTab", isFansTab);

        if (isLoggedIn && !isOwner) {
            model.addAttribute("isFollowed", userRelationService.isFollowed(loginUserId, pageUserId));
        } else {
            model.addAttribute("isFollowed", Boolean.FALSE);
        }
    }

    /**
     * 返回Home页选择列表标签
     *
     * @param selectType
     * @param isAuthor true 表示当前为查看自己的个人主页
     * @return
     */
    private List<TagSelectDTO> homeSelectTags(String selectType, boolean isAuthor) {
        List<TagSelectDTO> tags = new ArrayList<>();
        homeSelectTags.forEach(tag -> {
            HomeSelectEnum selectEnum = HomeSelectEnum.fromCode(tag);
            if (selectEnum == null) {
                TagSelectDTO tagSelectDTO = new TagSelectDTO();
                tagSelectDTO.setTagName(tag);
                tagSelectDTO.setSelectDesc("fans".equals(tag) ? "粉丝" : tag);
                tagSelectDTO.setSelected(selectType.equals(tag));
                tags.add(tagSelectDTO);
                return;
            }
            TagSelectDTO tagSelectDTO = new TagSelectDTO();
            tagSelectDTO.setTagName(tag);
            tagSelectDTO.setSelectDesc(selectEnum.getDesc());
            tagSelectDTO.setSelected(selectType.equals(tag));
            tags.add(tagSelectDTO);
        });
        return tags;
    }

    /**
     * 返回关注用户选择列表标签
     *
     * @param selectType
     * @return
     */
    private List<TagSelectDTO> followSelectTags(String selectType) {
        List<TagSelectDTO> tags = new ArrayList<>();
        followSelectTags.forEach(tag -> {
            TagSelectDTO tagSelectDTO = new TagSelectDTO();
            tagSelectDTO.setTagName(tag);
            tagSelectDTO.setSelectDesc(FollowSelectEnum.fromCode(tag).getDesc());
            tagSelectDTO.setSelected(selectType.equals(tag));
            tags.add(tagSelectDTO);
        });
        return tags;
    }

    /**
     * 返回选择列表
     *
     * @param vo
     * @param userId
     */
    private void userHomeSelectList(UserHomeVo vo, Long userId) {
        PageParam pageParam = PageParam.newPageInstance();
        String selectType = vo.getHomeSelectType();

        if ("follow".equals(selectType)) {
            vo.setFollowSelectType(FollowTypeEnum.FOLLOW.getCode());
            initFollowFansList(vo, userId, pageParam);
            return;
        }
        if ("fans".equals(selectType)) {
            vo.setFollowSelectType(FollowTypeEnum.FANS.getCode());
            initFollowFansList(vo, userId, pageParam);
            return;
        }

        HomeSelectEnum select = HomeSelectEnum.fromCode(selectType);
        if (select == null) {
            return;
        }

        switch (select) {
            case ARTICLE:
                PageListVo<ArticleDTO> dto = articleReadService.queryArticlesByUserAndType(userId, pageParam, select);
                vo.setHomeSelectList(dto);
                return;
            case COLLECT:
                PageListVo<ArticleDTO> collectDto = articleReadService.queryArticlesByUserAndType(userId, pageParam, select);
                vo.setHomeSelectList(collectDto);
                return;
            default:
                vo.setHomeSelectList(PageListVo.emptyVo());
        }
    }

    private void initFollowFansList(UserHomeVo vo, long userId, PageParam pageParam) {
        PageListVo<FollowUserInfoDTO> followList;
        boolean needUpdateRelation = false;
        if (vo.getFollowSelectType().equals(FollowTypeEnum.FOLLOW.getCode())) {
            followList = userRelationService.getUserFollowList(userId, pageParam);
        } else {
            followList = userRelationService.getUserFansList(userId, pageParam);
            needUpdateRelation = true;
        }

        Long loginUserId = ReqInfoContext.getReqInfo() != null ? ReqInfoContext.getReqInfo().getUserId() : null;
        if (loginUserId != null && (!Objects.equals(loginUserId, userId) || needUpdateRelation)) {
            userRelationService.updateUserFollowRelationId(followList, loginUserId);
        }
        vo.setFollowList(followList);
    }


    @GetMapping("pay")
    @Permission(role = UserRole.LOGIN)
    public String pay() {
        return  "views/user/pay-item";
    }
}

