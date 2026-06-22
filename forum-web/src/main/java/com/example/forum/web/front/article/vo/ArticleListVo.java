package com.example.forum.web.front.article.vo;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import lombok.Data;

/**
 * @author dev
 * @date 2022/10/28
 */
@Data
public class ArticleListVo {
    /**
     * 归档类型
     */
    private String archives;
    /**
     * 归档id
     */
    private Long archiveId;

    private PageListVo<ArticleDTO> articles;
    
    /**
     * 当前分类名称
     */
    private String currentCategory;

    /**
     * 当前标签名称
     */
    private String currentTag;
}
