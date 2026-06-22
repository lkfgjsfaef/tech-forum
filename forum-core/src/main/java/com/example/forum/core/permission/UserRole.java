package com.example.forum.core.permission;

public enum UserRole {
    GUEST(0, "游客"),
    ALL(-1, "所有人"),
    LOGIN(1, "登录用户"),
    LOGIN_USER(1, "登录用户"),
    ADMIN(2, "管理员");

    private final int code;
    private final String desc;

    UserRole(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}


