package com.example.forum.core.net;

import com.example.forum.core.config.ProxyProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProxyCenter {
    
    private static ProxyProperties proxyProperties;
    
    public ProxyCenter(ProxyProperties proxyProperties) {
        ProxyCenter.proxyProperties = proxyProperties;
    }
    
    public static void initProxyPool(List<ProxyProperties.ProxyItem> proxyList) {
        if (proxyList != null && !proxyList.isEmpty()) {
            log.info("初始化代理池，共 {} 个代理", proxyList.size());
        }
    }
    
    public boolean isProxyEnabled() {
        return proxyProperties != null && proxyProperties.isEnable();
    }
    
    public String getProxyHost() {
        return proxyProperties != null ? proxyProperties.getHost() : null;
    }
    
    public Integer getProxyPort() {
        return proxyProperties != null ? proxyProperties.getPort() : null;
    }
    
    public String getProxyUsername() {
        return proxyProperties != null ? proxyProperties.getUsername() : null;
    }
    
    public String getProxyPassword() {
        return proxyProperties != null ? proxyProperties.getPassword() : null;
    }
}


