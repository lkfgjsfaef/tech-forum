package com.example.forum.service.sensitive.service.impl;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.config.SensitiveWordConfigReq;
import com.example.forum.api.model.vo.config.SearchSensitiveWordHitReq;
import com.example.forum.api.model.vo.config.dto.SensitiveWordConfigDTO;
import com.example.forum.api.model.vo.config.dto.SensitiveWordHitDTO;
import com.example.forum.service.sensitive.service.SensitiveAdminService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SensitiveAdminServiceImpl implements SensitiveAdminService {

    @Override
    public SensitiveWordConfigDTO getConfig() {
        return new SensitiveWordConfigDTO();
    }

    @Override
    public PageVo<SensitiveWordHitDTO> getHitWordPage(SearchSensitiveWordHitReq req) {
        PageVo<SensitiveWordHitDTO> result = new PageVo<>();
        result.setList(new ArrayList<>());
        result.setTotal(0L);
        return result;
    }

    @Override
    public void saveConfig(SensitiveWordConfigReq req) {
    }

    @Override
    public void clearHitWord(String word) {
    }
}
