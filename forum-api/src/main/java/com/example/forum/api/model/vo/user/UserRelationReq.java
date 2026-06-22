package com.example.forum.api.model.vo.user;

import lombok.Data;

@Data
public class UserRelationReq {
    private Long userId;
    private Long followUserId;
    private Integer relationType;
}
