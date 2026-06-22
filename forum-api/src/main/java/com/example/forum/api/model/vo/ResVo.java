package com.example.forum.api.model.vo;

import com.example.forum.api.model.vo.constants.StatusEnum;
import lombok.Data;

@Data
public class ResVo<T> {
    private Status status;
    private T result;

    public static <T> ResVo<T> ok(T result) {
        ResVo<T> vo = new ResVo<>();
        vo.setStatus(Status.newStatus(StatusEnum.SUCCESS));
        vo.setResult(result);
        return vo;
    }

    public static <T> ResVo<T> ok() {
        return ok(null);
    }

    public static <T> ResVo<T> fail(Status status) {
        ResVo<T> vo = new ResVo<>();
        vo.setStatus(status);
        return vo;
    }

    public static <T> ResVo<T> fail(StatusEnum statusEnum) {
        return fail(Status.newStatus(statusEnum));
    }

    public static <T> ResVo<T> fail(StatusEnum statusEnum, String msg) {
        return fail(Status.newStatus(statusEnum.getCode(), msg));
    }
}
