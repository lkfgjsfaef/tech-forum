package com.example.forum.api.model.vo.seo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seo {
    private String title;
    private String description;
    private String keywords;
    private String ogTitle;
    private String ogDescription;
    private String ogImage;
    private List<SeoTagVo> ogp;
    private Map<String, Object> jsonLd;
}
