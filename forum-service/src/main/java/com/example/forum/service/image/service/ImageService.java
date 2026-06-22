package com.example.forum.service.image.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String upload(MultipartFile file);
    String upload(byte[] content, String fileName);
    String saveImg(HttpServletRequest request);
    String saveImg(String imgUrl);
}
