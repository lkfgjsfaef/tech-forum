package com.example.forum.api.model.exception;

import com.example.forum.api.model.vo.Status;
import com.example.forum.api.model.vo.constants.StatusEnum;

public class ExceptionUtil {

    public static ForumException of(StatusEnum statusEnum, String message) {
        return new ForumException(Status.newStatus(statusEnum, message));
    }

    public static ForumException of(Status status) {
        return new ForumException(status);
    }
}
