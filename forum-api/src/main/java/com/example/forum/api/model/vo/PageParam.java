package com.example.forum.api.model.vo;

import lombok.Data;
import java.util.List;

@Data
public class PageParam {
    public static final Long DEFAULT_PAGE_NUM = 1L;
    public static final Long DEFAULT_PAGE_SIZE = 10L;
    public static final int TOP_PAGE_SIZE = 3;

    private Integer pageNum = 1;
    private Integer pageSize = 10;

    public long getOffset() {
        return (long) (pageNum - 1) * pageSize;
    }

    public static PageParam newPageInstance(Long page, Long pageSize) {
        PageParam param = new PageParam();
        if (page != null && page > 0) {
            param.setPageNum(page.intValue());
        }
        if (pageSize != null && pageSize > 0) {
            param.setPageSize(pageSize.intValue());
        }
        return param;
    }

    public static PageParam newPageInstance() {
        return new PageParam();
    }
}
