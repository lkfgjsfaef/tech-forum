package com.example.forum.api.model.vo.wx.menu;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WxMenuItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String type;
    private String key;
    private String url;
    private String appId;
    private String pagePath;
    private List<WxMenuItemDTO> subButtons;
}
