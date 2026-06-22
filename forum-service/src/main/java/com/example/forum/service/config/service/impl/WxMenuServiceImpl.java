package com.example.forum.service.config.service.impl;

import com.example.forum.api.model.vo.wx.menu.*;
import com.example.forum.service.config.service.WxMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WxMenuServiceImpl implements WxMenuService {

    @Override
    public void syncMenu() {
    }

    @Override
    public void deleteMenu() {
    }

    @Override
    public WxMenuDetailDTO getDetail() {
        WxMenuDetailDTO dto = new WxMenuDetailDTO();
        dto.setMenuItems(new ArrayList<>());
        return dto;
    }

    @Override
    public void saveDraft(WxMenuSaveReq req) {
    }

    @Override
    public WxMenuValidateResDTO validate(WxMenuValidateReq request) {
        WxMenuValidateResDTO dto = new WxMenuValidateResDTO();
        dto.setValid(true);
        dto.setErrors(new ArrayList<>());
        return dto;
    }

    @Override
    public WxMenuPublishResDTO publish(WxMenuPublishReq request) {
        WxMenuPublishResDTO dto = new WxMenuPublishResDTO();
        dto.setSuccess(true);
        dto.setMessage("发布成功");
        return dto;
    }

    @Override
    public WxMenuDetailDTO syncRemoteToDraft() {
        WxMenuDetailDTO dto = new WxMenuDetailDTO();
        dto.setMenuItems(new ArrayList<>());
        return dto;
    }

    @Override
    public WxMenuPreviewMatchResDTO previewMatch(WxMenuPreviewMatchReq request) {
        WxMenuPreviewMatchResDTO dto = new WxMenuPreviewMatchResDTO();
        dto.setMatched(false);
        return dto;
    }

    @Override
    public WxMenuPreviewAiResDTO previewAi(WxMenuPreviewAiReq request) {
        WxMenuPreviewAiResDTO dto = new WxMenuPreviewAiResDTO();
        dto.setGeneratedMenu("");
        dto.setExplanation("");
        return dto;
    }
}
