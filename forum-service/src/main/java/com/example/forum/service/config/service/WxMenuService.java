package com.example.forum.service.config.service;

import com.example.forum.api.model.vo.wx.menu.WxMenuDetailDTO;
import com.example.forum.api.model.vo.wx.menu.WxMenuPreviewAiReq;
import com.example.forum.api.model.vo.wx.menu.WxMenuPreviewAiResDTO;
import com.example.forum.api.model.vo.wx.menu.WxMenuPreviewMatchReq;
import com.example.forum.api.model.vo.wx.menu.WxMenuPreviewMatchResDTO;
import com.example.forum.api.model.vo.wx.menu.WxMenuPublishReq;
import com.example.forum.api.model.vo.wx.menu.WxMenuPublishResDTO;
import com.example.forum.api.model.vo.wx.menu.WxMenuSaveReq;
import com.example.forum.api.model.vo.wx.menu.WxMenuValidateReq;
import com.example.forum.api.model.vo.wx.menu.WxMenuValidateResDTO;

public interface WxMenuService {
    void syncMenu();
    void deleteMenu();
    WxMenuDetailDTO getDetail();
    void saveDraft(WxMenuSaveReq req);
    WxMenuValidateResDTO validate(WxMenuValidateReq request);
    WxMenuPublishResDTO publish(WxMenuPublishReq request);
    WxMenuDetailDTO syncRemoteToDraft();
    WxMenuPreviewMatchResDTO previewMatch(WxMenuPreviewMatchReq request);
    WxMenuPreviewAiResDTO previewAi(WxMenuPreviewAiReq request);
}
