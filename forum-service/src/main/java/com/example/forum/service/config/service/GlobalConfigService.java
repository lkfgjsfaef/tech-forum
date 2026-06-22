package com.example.forum.service.config.service;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.config.GlobalConfigReq;
import com.example.forum.api.model.vo.config.SearchGlobalConfigReq;
import com.example.forum.api.model.vo.config.dto.GlobalConfigDTO;

public interface GlobalConfigService {
    PageVo<GlobalConfigDTO> getConfigList(SearchGlobalConfigReq req);
    PageVo<GlobalConfigDTO> getList(SearchGlobalConfigReq req);
    GlobalConfigDTO getConfigDetail(Long id);
    void saveConfig(GlobalConfigReq req);
    void save(GlobalConfigReq req);
    void deleteConfig(Long id);
    void delete(Long id);
    void addSensitiveWhiteWord(String word);
}
