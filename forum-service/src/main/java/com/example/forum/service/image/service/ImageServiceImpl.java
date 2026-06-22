package com.example.forum.service.image.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public String upload(MultipartFile file) {
        return "/upload/" + file.getOriginalFilename();
    }

    @Override
    public String upload(byte[] content, String fileName) {
        return "/upload/" + fileName;
    }

    @Override
    public String saveImg(HttpServletRequest request) {
        return "/upload/" + UUID.randomUUID().toString();
    }

    @Override
    public String saveImg(String imgUrl) {
        return imgUrl;
    }

    public byte[] download(String imageUrl) {
        return new byte[0];
    }
}
