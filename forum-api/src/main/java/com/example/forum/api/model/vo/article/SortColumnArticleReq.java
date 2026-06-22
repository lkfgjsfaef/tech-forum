package com.example.forum.api.model.vo.article;

import lombok.Data;

import java.util.List;

@Data
public class SortColumnArticleReq {
    private Long columnId;
    private List<Long> articleIds;
}
