package com.example.forum.service.pay;

import com.example.forum.api.model.enums.pay.ThirdPayWayEnum;
import com.example.forum.service.pay.service.ThirdPayHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceFactory {
    private final Map<ThirdPayWayEnum, ThirdPayHandler> handlers = new HashMap<>();
    
    public void register(ThirdPayWayEnum way, ThirdPayHandler handler) {
        handlers.put(way, handler);
    }
    
    public ThirdPayHandler getHandler(ThirdPayWayEnum way) {
        return handlers.get(way);
    }

    public ThirdPayHandler getPayService(ThirdPayWayEnum way) {
        return getHandler(way);
    }
}
