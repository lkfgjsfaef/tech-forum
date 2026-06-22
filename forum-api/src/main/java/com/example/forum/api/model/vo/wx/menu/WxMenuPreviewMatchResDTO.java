package com.example.forum.api.model.vo.wx.menu;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxMenuPreviewMatchResDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean matched;
    private String matchedMenu;
    private String matchedKey;
}
