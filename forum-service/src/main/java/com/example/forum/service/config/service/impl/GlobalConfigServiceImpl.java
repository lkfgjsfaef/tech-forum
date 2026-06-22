package com.example.forum.service.config.service.impl;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.config.GlobalConfigReq;
import com.example.forum.api.model.vo.config.SearchGlobalConfigReq;
import com.example.forum.api.model.vo.config.dto.GlobalConfigDTO;
import com.example.forum.service.config.service.GlobalConfigService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GlobalConfigServiceImpl implements GlobalConfigService {

    @Override
    public PageVo<GlobalConfigDTO> getConfigList(SearchGlobalConfigReq req) {
        PageVo<GlobalConfigDTO> result = new PageVo<>();
        result.setList(new ArrayList<>());
        result.setTotal(0L);
        return result;
    }

    @Override
    public PageVo<GlobalConfigDTO> getList(SearchGlobalConfigReq req) {
        return getConfigList(req);
    }

    @Override
    public GlobalConfigDTO getConfigDetail(Long id) {
        return null;
    }

    @Override
    public void saveConfig(GlobalConfigReq req) {
    }

    @Override
    public void save(GlobalConfigReq req) {
        saveConfig(req);
    }

    @Override
    public void deleteConfig(Long id) {
    }

    @Override
    public void delete(Long id) {
        deleteConfig(id);
    }

    @Override
    public void addSensitiveWhiteWord(String word) {
    }
}
