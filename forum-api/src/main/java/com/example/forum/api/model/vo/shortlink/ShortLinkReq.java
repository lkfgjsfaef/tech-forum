package com.example.forum.api.model.vo.shortlink;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShortLinkReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private String originalUrl;
}
