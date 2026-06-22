package com.example.forum.service.config.service.impl;

import com.example.forum.api.model.enums.ConfigTypeEnum;
import com.example.forum.api.model.vo.banner.dto.ConfigDTO;
import com.example.forum.service.config.service.ConfigService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Override
    public List<ConfigDTO> getConfigList(ConfigTypeEnum type) {
        return new ArrayList<>();
    }

    @Override
    public ConfigDTO getConfig(Long configId) {
        return null;
    }

    @Override
    public Long saveConfig(ConfigDTO configDTO) {
        return 1L;
    }

    @Override
    public void updateConfig(ConfigDTO configDTO) {
    }

    @Override
    public void deleteConfig(Long configId) {
    }
}
