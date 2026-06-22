package com.example.forum.service.article.service;

import com.example.forum.api.model.vo.article.AiSeoGenerateReq;
import com.example.forum.api.model.vo.article.AiSeoGenerateRes;
import com.example.forum.api.model.vo.article.AiSlugGenerateReq;
import org.springframework.stereotype.Service;

@Service
public interface AiSeoService {
    AiSeoGenerateRes generateSeo(AiSeoGenerateReq req);
    AiSeoGenerateRes generateSeoTitleAndDescription(AiSeoGenerateReq req);
    String generateSlug(AiSlugGenerateReq req);
}
