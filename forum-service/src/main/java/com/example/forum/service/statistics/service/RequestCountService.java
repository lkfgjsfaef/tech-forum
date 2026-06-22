package com.example.forum.service.statistics.service;

import com.example.forum.api.model.vo.PageParam;
import com.example.forum.service.statistics.repository.entity.RequestCountDO;

import java.util.List;

public interface RequestCountService {
    void increment();
    long getCount();
    long count();
    List<RequestCountDO> listRequestCount(PageParam pageParam);
}
