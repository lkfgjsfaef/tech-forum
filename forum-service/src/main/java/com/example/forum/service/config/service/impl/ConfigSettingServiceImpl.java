package com.example.forum.service.config.service.impl;

import com.example.forum.api.model.enums.PushStatusEnum;
import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.banner.ConfigReq;
import com.example.forum.api.model.vo.banner.SearchConfigReq;
import com.example.forum.api.model.vo.banner.dto.ConfigDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ConfigSettingServiceImpl {

    public void saveConfig(ConfigReq configReq) {
    }

    public void deleteConfig(Integer configId) {
    }

    public void operateConfig(Integer configId, Integer pushStatus) {
    }

    public PageVo<ConfigDTO> getConfigList(SearchConfigReq req) {
        PageVo<ConfigDTO> result = new PageVo<>();
        result.setList(new ArrayList<>());
        result.setTotal(0L);
        return result;
    }
}
