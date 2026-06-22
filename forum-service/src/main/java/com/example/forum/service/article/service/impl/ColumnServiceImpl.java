package com.example.forum.service.article.service.impl;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.article.dto.ColumnDTO;
import com.example.forum.api.model.vo.article.dto.SimpleArticleDTO;
import com.example.forum.service.article.entity.Article;
import com.example.forum.service.article.mapper.ArticleMapper;
import com.example.forum.service.article.repository.dao.ColumnArticleDao;
import com.example.forum.service.article.repository.dao.ColumnInfoDao;
import com.example.forum.service.article.repository.entity.ColumnArticleDO;
import com.example.forum.service.article.repository.entity.ColumnInfoDO;
import com.example.forum.service.article.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    private ColumnInfoDao columnInfoDao;

    @Autowired
    private ColumnArticleDao columnArticleDao;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PageListVo<ColumnDTO> listColumn(PageParam pageParam) {
        PageListVo<ColumnDTO> result = new PageListVo<>();
        long offset = (pageParam.getPageNum() - 1) * pageParam.getPageSize();
        List<ColumnInfoDO> columns = columnInfoDao.findAllPublished(offset, pageParam.getPageSize());
        int total = columnInfoDao.countAllPublished();

        List<ColumnDTO> dtoList = columns.stream().map(this::toColumnDTO).collect(Collectors.toList());

        result.setList(dtoList);
        result.setHasMore((long) total > offset + columns.size());
        return result;
    }

    @Override
    public List<SimpleArticleDTO> queryColumnArticles(Long columnId) {
        List<ColumnArticleDO> relations = columnArticleDao.findByColumnId(columnId);
        List<SimpleArticleDTO> articles = new ArrayList<>();
        for (ColumnArticleDO rel : relations) {
            SimpleArticleDTO dto = new SimpleArticleDTO();
            dto.setArticleId(rel.getArticleId());
            // 册"article 表获取标册"
            try {
                var article = articleMapper.findById(rel.getArticleId());
                if (article != null) {
                    dto.setTitle(article.getTitle());
                } else {
                    dto.setTitle("未知文章");
                }
            } catch (Exception e) {
                dto.setTitle("未知文章");
            }
            dto.setSection(rel.getSection());
            articles.add(dto);
        }
        return articles;
    }

    @Override
    public ColumnDTO queryColumnInfo(Long columnId) {
        ColumnInfoDO col = columnInfoDao.findById(columnId);
        if (col == null) {
            return null;
        }
        ColumnDTO dto = toColumnDTO(col);
        // 查询专栏文章列表
        List<SimpleArticleDTO> articles = queryColumnArticles(columnId);
        dto.setArticles(articles);
        return dto;
    }

    @Override
    public ColumnDTO queryBasicColumnInfo(Long columnId) {
        ColumnInfoDO col = columnInfoDao.findById(columnId);
        if (col == null) {
            return null;
        }
        return toColumnDTO(col);
    }

    @Override
    public void saveColumn(ColumnDTO columnDTO) {
        ColumnInfoDO col = new ColumnInfoDO();
        col.setColumnName(columnDTO.getColumnName());  // 册"使用正确的字段名
        col.setUserId(columnDTO.getAuthor());
        col.setIntroduction(columnDTO.getIntroduction());
        col.setCover(columnDTO.getCover());
        col.setSection(1);  // 默认值"册"

        // 设置默认值"
        if (columnDTO.getState() == null) {
            col.setState(1);  // 默认已发册"
        } else {
            col.setState(columnDTO.getState());
        }
        
        if (columnDTO.getNums() == null) {
            col.setNums(10);  // 默认10册"
        } else {
            col.setNums(columnDTO.getNums());
        }
        
        if (columnDTO.getType() == null) {
            col.setType(0);  // 默认免费
        } else {
            col.setType(columnDTO.getType());
        }
        
        columnInfoDao.insert(col);
    }

    @Override
    public void deleteColumn(Long columnId) {
        columnInfoDao.deleteById(columnId);
    }

    @Override
    public ColumnArticleDO getColumnArticleRelation(Long articleId) {
        return columnArticleDao.findByArticleId(articleId);
    }

    @Override
    public ColumnArticleDO queryColumnArticle(Long columnId, Integer section) {
        return columnArticleDao.findByColumnIdAndSection(columnId, section);
    }

    @Override
    public List<ColumnDTO> queryMyColumns(Long userId) {
        List<ColumnInfoDO> columns = columnInfoDao.findByUserId(userId);
        if (columns == null || columns.isEmpty()) {
            return new ArrayList<>();
        }
        return columns.stream().map(this::toColumnDTO).collect(Collectors.toList());
    }

    private ColumnDTO toColumnDTO(ColumnInfoDO col) {
        ColumnDTO dto = new ColumnDTO();
        dto.setColumnId(col.getId());
        String name = col.getColumnName();
        dto.setColumn(name);
        dto.setColumnName(name);
        dto.setAuthor(col.getUserId());
        dto.setIntroduction(col.getIntroduction());
        dto.setCover(col.getCover());
        dto.setState(col.getState());
        dto.setNums(col.getNums());
        dto.setType(col.getType());
        if (col.getPublishTime() != null) {
            dto.setPublishTime(java.sql.Timestamp.valueOf(col.getPublishTime()).getTime());
        }
        if (col.getFreeStartTime() != null) {
            dto.setFreeStartTime(java.sql.Timestamp.valueOf(col.getFreeStartTime()).getTime());
        }
        if (col.getFreeEndTime() != null) {
            dto.setFreeEndTime(java.sql.Timestamp.valueOf(col.getFreeEndTime()).getTime());
        }
        return dto;
    }

    @Override
    public List<ColumnDTO> searchColumns(String keyword) {
        List<ColumnInfoDO> columns = columnInfoDao.searchByKeyword(keyword);
        if (columns == null || columns.isEmpty()) {
            return new ArrayList<>();
        }
        return columns.stream().map(this::toColumnDTO).collect(Collectors.toList());
    }

    @Override
    public List<Article> getMyArticles(Long userId) {
        return articleMapper.findByUserId(userId);
    }

    @Override
    public void addArticleToColumn(Long columnId, Long articleId) {
        ColumnArticleDO existing = columnArticleDao.findByColumnAndArticle(columnId, articleId);
        if (existing != null) {
            throw new RuntimeException("该文章已在专栏中");
        }
        Integer maxSection = columnArticleDao.getMaxSection(columnId);
        ColumnArticleDO columnArticle = new ColumnArticleDO();
        columnArticle.setColumnId(columnId);
        columnArticle.setArticleId(articleId);
        columnArticle.setSection(maxSection + 1);
        columnArticle.setStatus(0);
        columnArticleDao.insert(columnArticle);
    }

    @Override
    public void removeArticleFromColumn(Long columnId, Long articleId) {
        columnArticleDao.deleteByColumnAndArticle(columnId, articleId);
    }
}


