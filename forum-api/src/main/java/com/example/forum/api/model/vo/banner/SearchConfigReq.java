package com.example.forum.api.model.vo.banner;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchConfigReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer pageNumber;
    private Integer pageSize;
    private String key;
    private Integer type;
}
