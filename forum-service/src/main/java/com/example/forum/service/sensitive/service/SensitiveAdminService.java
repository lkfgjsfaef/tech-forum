package com.example.forum.service.sensitive.service;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.config.SensitiveWordConfigReq;
import com.example.forum.api.model.vo.config.SearchSensitiveWordHitReq;
import com.example.forum.api.model.vo.config.dto.SensitiveWordConfigDTO;
import com.example.forum.api.model.vo.config.dto.SensitiveWordHitDTO;

public interface SensitiveAdminService {
    SensitiveWordConfigDTO getConfig();
    PageVo<SensitiveWordHitDTO> getHitWordPage(SearchSensitiveWordHitReq req);
    void saveConfig(SensitiveWordConfigReq req);
    void clearHitWord(String word);
}
