package com.example.forum.api.model.vo.chat;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatSourceOptionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String value;
    private String name;
}
