package com.example.forum.web.front.article.vo;

import com.example.forum.api.model.vo.article.dto.ArticleDTO;
import com.example.forum.api.model.vo.article.dto.CategoryDTO;
import com.example.forum.api.model.vo.article.dto.ColumnDTO;
import com.example.forum.api.model.vo.article.dto.TagDTO;
import lombok.Data;

import java.util.List;

/**
 * @author dev
 * @date 2022/9/2
 */
@Data
public class ArticleEditVo {

    private ArticleDTO article;

    private List<CategoryDTO> categories;

    private List<TagDTO> tags;

    private List<ColumnDTO> columns;

}
