package com.example.forum.service.statistics.service.impl;

import com.example.forum.api.model.vo.PageParam;
import com.example.forum.service.statistics.repository.entity.RequestCountDO;
import com.example.forum.service.statistics.service.RequestCountService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CountServiceImpl implements RequestCountService {
    private final AtomicLong counter = new AtomicLong(0);
    
    @Override
    public void increment() {
        counter.incrementAndGet();
    }
    
    @Override
    public long getCount() {
        return counter.get();
    }

    @Override
    public long count() {
        return counter.get();
    }

    @Override
    public List<RequestCountDO> listRequestCount(PageParam pageParam) {
        return new ArrayList<>();
    }

    public void autoRefreshAllUserStatisticInfo() {
    }
}
