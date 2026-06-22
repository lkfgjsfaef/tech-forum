package com.example.forum.api.model.vo.article;

import lombok.Data;

@Data
public class MoveColumnArticleOrGroupReq {
    private Long columnId;
    private Long id;
    private Integer type;
    private Integer direction;
}
