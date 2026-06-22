package com.example.forum.api.model.vo.user;

import lombok.Data;

import java.util.List;

@Data
public class ZsxqUserBatchOperateReq {
    private List<Long> ids;
    private Integer status;
}
