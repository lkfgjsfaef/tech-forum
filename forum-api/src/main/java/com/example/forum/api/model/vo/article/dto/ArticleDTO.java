package com.example.forum.api.model.vo.article.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleDTO {
    private Long articleId;
    private String title;
    private String shortTitle;
    private String summary;
    private String content;
    private String cover;
    private String author;
    private String authorName;
    private String authorAvatar;
    private Long authorId;
    private Integer readType;
    private Integer readCount;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long lastUpdateTime;
    private List<TagDTO> tags;
    private CategoryDTO category;
    private String slug;
    private String source;
    private Integer sourceType;
    private String sourceUrl;
    private Integer status;
    private Integer payStatus;
    private Boolean canRead;
    private Long categoryId;
    private List<Long> tagIds;
    private Boolean liked;  // 当前登录用户是否已点册"
    private Integer collectCount; // 收藏数
    private Boolean collected;   // 当前登录用户是否已收册"

    public String getUrlSlug() {
        return this.slug;
    }
}

