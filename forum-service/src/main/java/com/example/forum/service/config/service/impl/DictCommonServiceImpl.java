package com.example.forum.service.config.service.impl;

import com.example.forum.service.config.service.DictCommonService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DictCommonServiceImpl implements DictCommonService {

    @Override
    public Map<String, Object> getDict() {
        return new HashMap<>();
    }
}
