package com.example.forum.service.config.service;

import com.example.forum.api.model.enums.ConfigTypeEnum;
import com.example.forum.api.model.vo.banner.dto.ConfigDTO;

import java.util.List;

public interface ConfigService {
    List<ConfigDTO> getConfigList(ConfigTypeEnum type);
    ConfigDTO getConfig(Long configId);
    Long saveConfig(ConfigDTO configDTO);
    void updateConfig(ConfigDTO configDTO);
    void deleteConfig(Long configId);
}
