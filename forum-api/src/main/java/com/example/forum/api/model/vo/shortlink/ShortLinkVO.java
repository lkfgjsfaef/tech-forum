package com.example.forum.api.model.vo.shortlink;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShortLinkVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String shortUrl;
    private String originalUrl;

    public ShortLinkVO() {
    }

    public ShortLinkVO(String shortUrl, String originalUrl) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
    }
}
