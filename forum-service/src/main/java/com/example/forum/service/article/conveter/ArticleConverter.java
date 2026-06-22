package com.example.forum.service.article.conveter;

import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.service.article.repository.entity.ArticleDO;

public class ArticleConverter {
    public static ArticleDTO toDto(ArticleDO entity) {
        if (entity == null) return null;
        ArticleDTO dto = new ArticleDTO();
        dto.setArticleId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setSummary(entity.getSummary());
        dto.setCover(entity.getCover());
        dto.setAuthorId(entity.getAuthorId());
        dto.setReadCount(entity.getReadCount());
        dto.setLikeCount(entity.getLikeCount());
        dto.setCommentCount(entity.getCommentCount());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setSlug(entity.getSlug());
        dto.setStatus(entity.getStatus());
        dto.setPayStatus(entity.getPayStatus());
        return dto;
    }
    
    public static ArticleDO toDO(ArticleDTO dto) {
        if (dto == null) return null;
        ArticleDO entity = new ArticleDO();
        entity.setId(dto.getArticleId());
        entity.setTitle(dto.getTitle());
        entity.setSummary(dto.getSummary());
        entity.setCover(dto.getCover());
        entity.setAuthorId(dto.getAuthorId());
        entity.setReadCount(dto.getReadCount());
        entity.setLikeCount(dto.getLikeCount());
        entity.setCommentCount(dto.getCommentCount());
        entity.setCreateTime(dto.getCreateTime());
        entity.setUpdateTime(dto.getUpdateTime());
        entity.setSlug(dto.getSlug());
        entity.setStatus(dto.getStatus());
        entity.setPayStatus(dto.getPayStatus());
        return entity;
    }
}
