package com.example.forum.api.model.vo.constants;

import lombok.Getter;

@Getter
public enum StatusEnum {
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    ILLEGAL_ARGUMENTS(400, "参数错误"),
    ILLEGAL_ARGUMENTS_MIXED(400, "参数错误"),
    FORBID_ERROR(403, "无权限访册"),
    FORBID_ERROR_MIXED(403, "无权限访册"),
    FORBID_NOTLOGIN(403, "未登册"),
    RECORDS_NOT_EXISTS(404, "记录不存在"),
    UNEXPECT_ERROR(500, "服务器异常"),
    LOGIN_FAILED_MIXED(400, "登录失败"),
    UPLOAD_PIC_FAILED(500, "图片上传失败");

    private final int code;
    private final String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static boolean is5xx(int code) {
        return code >= 500 && code < 600;
    }

    public static boolean is403(int code) {
        return code == 403;
    }
}


