package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.article.AiSeoGenerateReq;
import com.example.forum.api.model.vo.article.AiSeoGenerateRes;
import com.example.forum.api.model.vo.article.AiSlugGenerateReq;
import com.example.forum.service.article.service.AiSeoService;
import org.springframework.stereotype.Service;

@Service
public class AiSeoServiceImpl implements AiSeoService {

    @Override
    public AiSeoGenerateRes generateSeo(AiSeoGenerateReq req) {
        AiSeoGenerateRes res = new AiSeoGenerateRes();
        res.setTitle("");
        res.setDescription("");
        res.setKeywords("");
        return res;
    }

    @Override
    public AiSeoGenerateRes generateSeoTitleAndDescription(AiSeoGenerateReq req) {
        return generateSeo(req);
    }

    @Override
    public String generateSlug(AiSlugGenerateReq req) {
        return "";
    }
}
