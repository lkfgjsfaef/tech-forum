package com.example.forum.web.openapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "openapi")
public class OpenApiProperties {

    private List<String> appIdList = new ArrayList<>();
    private String ipWhiteList;

    public List<String> appIdList() {
        return appIdList;
    }

    public List<String> ipWhiteList() {
        if (ipWhiteList == null || ipWhiteList.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(ipWhiteList.split(","));
    }

    public List<String> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<String> appIdList) {
        this.appIdList = appIdList;
    }

    public String getIpWhiteList() {
        return ipWhiteList;
    }

    public void setIpWhiteList(String ipWhiteList) {
        this.ipWhiteList = ipWhiteList;
    }
}
