package com.example.forum.api.model.vo.wx.menu;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxMenuPublishResDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private String errcode;
    private String errmsg;
}
