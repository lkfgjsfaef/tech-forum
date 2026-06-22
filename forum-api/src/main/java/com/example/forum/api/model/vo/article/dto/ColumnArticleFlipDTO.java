package com.example.forum.api.model.vo.article.dto;

import lombok.Data;

@Data
public class ColumnArticleFlipDTO {
    private String prevHref;
    private boolean prevShow;
    private String nextHref;
    private boolean nextShow;
}
