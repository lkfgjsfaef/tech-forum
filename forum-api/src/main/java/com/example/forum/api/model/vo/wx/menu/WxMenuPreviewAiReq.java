package com.example.forum.api.model.vo.wx.menu;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxMenuPreviewAiReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private String prompt;
    private String context;
}
