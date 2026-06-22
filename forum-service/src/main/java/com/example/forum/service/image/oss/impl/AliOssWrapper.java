package com.example.forum.service.image.oss.impl;

import org.springframework.stereotype.Component;

@Component
public class AliOssWrapper {
    public String upload(byte[] data, String fileName) {
        return "https://oss.aliyuncs.com/" + fileName;
    }
    
    public byte[] download(String url) {
        return new byte[0];
    }
}
