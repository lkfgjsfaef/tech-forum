package com.example.forum.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NextPageHtmlVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String html;
    private boolean hasMore;
}
