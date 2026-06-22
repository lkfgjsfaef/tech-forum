package com.example.forum.api.model.vo.wx.menu;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WxMenuValidateResDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean valid;
    private List<String> errors;
}
