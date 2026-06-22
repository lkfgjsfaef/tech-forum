package com.example.forum.api.model.vo;

import lombok.Data;
import java.util.List;

@Data
public class PageVo<T> {
    private List<T> list;
    private long total;
    private int pageNum;
    private int pageSize;
    private int pages;
    
    public static <T> PageVo<T> of(List<T> list, long total, int pageNum, int pageSize) {
        PageVo<T> vo = new PageVo<>();
        vo.setList(list);
        vo.setTotal(total);
        vo.setPageNum(pageNum);
        vo.setPageSize(pageSize);
        vo.setPages((int) Math.ceil((double) total / pageSize));
        return vo;
    }
}
