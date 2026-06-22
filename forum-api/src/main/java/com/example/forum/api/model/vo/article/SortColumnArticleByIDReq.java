package com.example.forum.api.model.vo.article;

import lombok.Data;

import java.util.List;

@Data
public class SortColumnArticleByIDReq {
    private Long columnId;
    private List<Long> articleIds;
}
