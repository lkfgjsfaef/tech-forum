package com.example.forum.web.front.login.pwd;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.constants.StatusEnum;
import com.example.forum.api.model.vo.user.UserPwdLoginReq;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.core.util.SessionUtil;
import com.example.forum.service.user.entity.User;
import com.example.forum.service.user.service.LoginService;
import com.example.forum.service.user.service.impl.LoginServiceImpl;
import com.example.forum.web.front.login.zsxq.helper.ZsxqHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * 用户和密码方式的登录/登出的入口
 *
 * @author dev
 * @date 2022/8/15
 */
@RestController
@RequestMapping
public class LoginRestController {
    
    private static final Logger log = LoggerFactory.getLogger(LoginRestController.class);

    @Autowired
    private LoginService loginService;
    
    @Autowired
    private LoginServiceImpl loginServiceImpl;
    
    @Autowired
    private ZsxqHelper zsxqHelper;

    /**
     * 用户名和密码登录
     */
    @PostMapping("/login/username")
    public ResVo<Boolean> login(@RequestParam(name = "username") String username,
                                @RequestParam(name = "password") String password,
                                HttpServletResponse response) {
        // 先检查用户是否存在
        User existCheck = loginServiceImpl.checkUserExists(username);
        if (existCheck == null) {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "账号不存在，请先注册");
        }
        String session = loginService.loginByUserPwd(username, password);
        if (StringUtils.isNotBlank(session)) {
            response.addCookie(SessionUtil.newCookie(LoginService.SESSION_KEY, session));
            return ResVo.ok(true);
        } else {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "密码错误");
        }
    }

    /**
     * 发送验证码（到邮箱，验证码显示在控制台）
     */
    @PostMapping("/login/sendCode")
    public ResVo<Boolean> sendVerifyCode(@RequestParam(name = "target") String target) {
        if (StringUtils.isBlank(target) || !target.contains("@")) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "请输入正确的邮箱地址");
        }
        loginServiceImpl.generateVerifyCode(target);
        return ResVo.ok(true);
    }

    /**
     * 验证码校验（仅验证验证码是否正确，不登录）
     */
    @PostMapping("/login/verifyCode")
    public ResVo<Boolean> verifyCode(@RequestParam(name = "target") String target,
                                     @RequestParam(name = "code") String code) {
        if (loginServiceImpl.verifyCode(target, code)) {
            return ResVo.ok(true);
        } else {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "验证码错误或已过期");
        }
    }

    /**
     * 验证码注册（未注册用户）
     */
    @PostMapping("/login/registerByCode")
    public ResVo<Long> registerByVerifyCode(@RequestParam(name = "username") String username,
                                           @RequestParam(name = "nickname") String nickname,
                                           @RequestParam(name = "target") String target,
                                           @RequestParam(name = "code") String code,
                                           HttpServletResponse response) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(nickname)) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "用户名和昵称不能为空");
        }
        if (!loginServiceImpl.verifyCode(target, code)) {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "验证码错误或已过期");
        }
        
        // 创建用户
        UserPwdLoginReq loginReq = new UserPwdLoginReq();
        loginReq.setUsername(username);
        loginReq.setPassword("");
        loginReq.setNickname(nickname);
        loginReq.setEmail(target);
        
        String session = loginService.registerByUserPwd(loginReq);
        if (StringUtils.isNotBlank(session)) {
            response.addCookie(SessionUtil.newCookie(LoginService.SESSION_KEY, session));
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            return ResVo.ok(userId);
        } else {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "用户名已存在)");
        }
    }

    /**
     * 用户名密码注册"
     */
    @PostMapping("/login/register")
    public ResVo<Long> register(UserPwdLoginReq loginReq,
                                HttpServletResponse response) {
        if (StringUtils.isBlank(loginReq.getUsername()) || StringUtils.isBlank(loginReq.getPassword())) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "用户名和密码不能为空");
        }
        if (loginReq.getPassword().length() < 6) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "密码长度不能少于6位");
        }
        
        String session = loginService.registerByUserPwd(loginReq);
        if ("EMAIL_EXISTS".equals(session)) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "该邮箱已被注册，请使用其他邮箱");
        }
        if (StringUtils.isNotBlank(session)) {
            response.addCookie(SessionUtil.newCookie(LoginService.SESSION_KEY, session));
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            return ResVo.ok(userId);
        } else {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "用户名已存在或注册失败");
        }
    }

    @Permission(role = UserRole.LOGIN)
    @RequestMapping("logout")
    public ResVo<Boolean> logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        Optional.ofNullable(ReqInfoContext.getReqInfo()).ifPresent(s -> loginService.logout(s.getSession()));
        SessionUtil.delCookies(LoginService.SESSION_KEY);
        String referer = request.getHeader("Referer");
        if (StringUtils.isBlank(referer)) {
            referer = "/";
        }
        response.sendRedirect(referer);
        return ResVo.ok(true);
    }

    /**
     * 知识星球登录
     */
    @RequestMapping("login/zsxq")
    public void redirectToZsxq(HttpServletResponse response) throws IOException {
        String url = zsxqHelper.buildZsxqLoginUrl("login");
        response.sendRedirect(url);
    }
}



