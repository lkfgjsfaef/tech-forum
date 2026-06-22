package com.example.forum.web.admin.rest;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import com.example.forum.api.model.vo.user.dto.SimpleUserInfoDTO;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.user.service.UserService;
import com.example.forum.web.front.search.vo.SearchUserVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户权限管理后台
 *
 * @author dev
 * @date 2022/9/19
 */
@RestController
@Permission(role = UserRole.ADMIN)
@Tag(name = "API")
@RequestMapping(path = {"api/admin/user/", "admin/user/"})
public class UserSettingRestController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户搜索")
    @GetMapping(path = "query")
    public ResVo<SearchUserVo> queryUserList(@RequestParam(name = "key", required = false) String key) {
        List<SimpleUserInfoDTO> list = userService.searchUser(key);
        SearchUserVo vo = new SearchUserVo();
        vo.setKey(key);
        vo.setItems(list);
        return ResVo.ok(vo);
    }

    @Permission(role = UserRole.LOGIN)
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("info")
    public ResVo<BaseUserInfoDTO> info() {
        BaseUserInfoDTO user = ReqInfoContext.getReqInfo().getUser();
        return ResVo.ok(user);
    }
}
