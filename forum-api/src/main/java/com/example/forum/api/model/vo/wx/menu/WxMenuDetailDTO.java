package com.example.forum.api.model.vo.wx.menu;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WxMenuDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<WxMenuItemDTO> menuItems;
    private String draftJson;
    private String remoteJson;
}
