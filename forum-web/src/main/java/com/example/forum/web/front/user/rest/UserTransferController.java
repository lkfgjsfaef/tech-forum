package com.example.forum.web.front.user.rest;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.user.service.UserTransferService;
import com.example.forum.web.front.login.zsxq.helper.ZsxqHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户账号迁移
 *
 * @author dev
 * @date 2025/9/29
 */
@Permission(role = UserRole.LOGIN)
@RestController
@RequestMapping("/user/api/transfer")
public class UserTransferController {

    @Autowired
    private UserTransferService userTransferService;

    @Autowired
    private ZsxqHelper zsxqHelper;

    /**
     * 用户名密码方式账号迁册"
     *
     * @param username 用户册"
     * @param password 密码
     * @return
     */
    @PostMapping("/userPwd")
    public ResVo<Long> transferByUserPwd(@RequestParam(name = "username") String username,
                                         @RequestParam(name = "password") String password,
                                         HttpServletResponse response) throws IOException {
        boolean ans = userTransferService.transferUser(username, password);
        return ans ? ResVo.ok(ReqInfoContext.getReqInfo().getUserId()) : ResVo.ok(0L);
    }

    @RequestMapping("/zsxq")
    public void transferByZsxq(HttpServletResponse response) throws IOException {
        String url = zsxqHelper.buildZsxqLoginUrl(ZsxqHelper.EXTRA_TAG_USER_TRANSFER);
        response.sendRedirect(url);
    }
}

