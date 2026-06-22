package com.example.forum.core.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SessionUtil {
    private static final String USER_SESSION_KEY = "user";
    private static final ThreadLocal<UserInfo> CURRENT_USER = new ThreadLocal<>();

    @Data
    public static class UserInfo {
        private Long userId;
        private String userName;
        private String nickName;
        private String photo;
        private String email;
        private Integer role;
    }

    public static UserInfo getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static Long getCurrentUserId() {
        UserInfo user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    public static void setCurrentUser(HttpServletRequest request, UserInfo user) {
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_SESSION_KEY, user);
        CURRENT_USER.set(user);
    }

    public static void setCurrentUser(UserInfo user) {
        CURRENT_USER.set(user);
    }

    public static void removeCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(USER_SESSION_KEY);
            session.invalidate();
        }
        CURRENT_USER.remove();
    }

    public static void clearCurrentUser() {
        CURRENT_USER.remove();
    }

    public static String getSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? session.getId() : null;
    }

    public static Cookie findCookieByName(HttpServletRequest request, String name) {
        if (request == null || request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    public static String findCookieByName(org.springframework.http.server.ServerHttpRequest request, String name) {
        if (request == null) return null;
        org.springframework.http.HttpHeaders headers = request.getHeaders();
        String cookieHeader = headers.getFirst(org.springframework.http.HttpHeaders.COOKIE);
        if (cookieHeader == null) return null;
        for (String cookie : cookieHeader.split(";")) {
            String trimmed = cookie.trim();
            int idx = trimmed.indexOf('=');
            if (idx > 0 && trimmed.substring(0, idx).trim().equals(name)) {
                return trimmed.substring(idx + 1).trim();
            }
        }
        return null;
    }

    public static List<Cookie> findCookiesByName(HttpServletRequest request, String name) {
        if (request == null || request.getCookies() == null) {
            return Collections.emptyList();
        }
        List<Cookie> result = new ArrayList<>();
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                result.add(cookie);
            }
        }
        return result;
    }

    public static Cookie newCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        return cookie;
    }

    public static Cookie newCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static void delCookie(Cookie cookie) {
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
        }
    }

    public static void delCookies(String... names) {
        if (names == null) {
            return;
        }
        HttpServletRequest request = SpringUtil.getCurrentRequest();
        if (request == null) {
            return;
        }
        HttpServletResponse response = SpringUtil.getCurrentResponse();
        if (response == null) {
            return;
        }
        for (String name : names) {
            Cookie cookie = findCookieByName(request, name);
            if (cookie != null) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }
}
