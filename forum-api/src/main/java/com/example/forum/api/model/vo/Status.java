package com.example.forum.api.model.vo;

import com.example.forum.api.model.vo.constants.StatusEnum;
import lombok.Data;

@Data
public class Status {
    private int code;
    private String msg;

    public static Status newStatus(int code, String msg) {
        Status status = new Status();
        status.setCode(code);
        status.setMsg(msg);
        return status;
    }

    public static Status newStatus(StatusEnum statusEnum) {
        return newStatus(statusEnum.getCode(), statusEnum.getMsg());
    }

    public static Status newStatus(StatusEnum statusEnum, String msg) {
        return newStatus(statusEnum.getCode(), msg);
    }
}
