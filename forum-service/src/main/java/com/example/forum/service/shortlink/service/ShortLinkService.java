package com.example.forum.service.shortlink.service;

import com.example.forum.api.model.vo.shortlink.ShortLinkVO;
import com.example.forum.api.model.vo.shortlink.dto.ShortLinkDTO;

public interface ShortLinkService {
    String createShortLink(String originalUrl);
    String getOriginalUrl(String shortCode);
    ShortLinkVO createShortLink(ShortLinkDTO shortLinkDTO);
    ShortLinkVO getOriginalLink(String shortCode);
}
