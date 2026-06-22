package com.example.forum.service.sitemap.model;

import lombok.Data;

@Data
public class SiteCntVo {
    private Long articleCount;
    private Long userCount;
    private Long commentCount;
    private Long visitCount;
    private Long pvCount;
    private Long uvCount;
}
