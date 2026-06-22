package com.example.forum.api.model.vo;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class PageListVo<T> {
    private List<T> list;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPages;
    private Boolean hasMore;

    public static <T> PageListVo<T> of(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        PageListVo<T> vo = new PageListVo<>();
        vo.setList(list);
        vo.setTotal(total);
        vo.setPageNum(pageNum);
        vo.setPageSize(pageSize);
        int totalPages = (int) Math.ceil((double) total / pageSize);
        vo.setTotalPages(totalPages);
        vo.setHasMore(pageNum < totalPages);
        return vo;
    }

    public static <T> PageListVo<T> emptyVo() {
        PageListVo<T> vo = new PageListVo<>();
        vo.setList(new ArrayList<>());
        vo.setTotal(0L);
        vo.setPageNum(1);
        vo.setPageSize(10);
        vo.setTotalPages(0);
        vo.setHasMore(false);
        return vo;
    }
}
