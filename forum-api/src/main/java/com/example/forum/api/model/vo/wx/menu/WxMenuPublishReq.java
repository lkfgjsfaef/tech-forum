package com.example.forum.api.model.vo.wx.menu;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxMenuPublishReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private String menuJson;
}
