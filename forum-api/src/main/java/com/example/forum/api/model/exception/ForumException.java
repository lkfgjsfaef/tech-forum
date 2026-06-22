package com.example.forum.api.model.exception;

import com.example.forum.api.model.vo.Status;
import com.example.forum.api.model.vo.constants.StatusEnum;
import lombok.Getter;

@Getter
public class ForumException extends RuntimeException {
    private final Status status;

    public ForumException(Status status) {
        super(status.getMsg());
        this.status = status;
    }

    public ForumException(StatusEnum statusEnum) {
        super(statusEnum.getMsg());
        this.status = Status.newStatus(statusEnum);
    }

    public ForumException(StatusEnum statusEnum, String msg) {
        super(msg);
        this.status = Status.newStatus(statusEnum, msg);
    }
}
