package com.example.forum.service.shortlink.service.impl;

import com.example.forum.api.model.vo.shortlink.ShortLinkVO;
import com.example.forum.api.model.vo.shortlink.dto.ShortLinkDTO;
import com.example.forum.service.shortlink.service.ShortLinkService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShortLinkServiceImpl implements ShortLinkService {

    @Override
    public String createShortLink(String originalUrl) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        return null;
    }

    @Override
    public ShortLinkVO createShortLink(ShortLinkDTO shortLinkDTO) {
        ShortLinkVO vo = new ShortLinkVO();
        vo.setShortUrl(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        vo.setOriginalUrl(shortLinkDTO != null ? shortLinkDTO.getOriginalUrl() : null);
        return vo;
    }

    @Override
    public ShortLinkVO getOriginalLink(String shortCode) {
        return null;
    }
}
