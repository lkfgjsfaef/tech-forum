package com.example.forum.api.model.exception;

import com.example.forum.api.model.vo.Status;
import com.example.forum.api.model.vo.constants.StatusEnum;

public class ForumAdviceException extends RuntimeException {
    private final Status status;

    public ForumAdviceException(Status status) {
        super(status.getMsg());
        this.status = status;
    }

    public ForumAdviceException(StatusEnum statusEnum) {
        super(statusEnum.getMsg());
        this.status = Status.newStatus(statusEnum);
    }

    public ForumAdviceException(StatusEnum statusEnum, String msg) {
        super(msg);
        this.status = Status.newStatus(statusEnum, msg);
    }

    public Status getStatus() {
        return status;
    }
}
