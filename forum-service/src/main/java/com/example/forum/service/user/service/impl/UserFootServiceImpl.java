package com.example.forum.service.user.service.impl;

import com.example.forum.api.model.enums.DocumentTypeEnum;
import com.example.forum.api.model.enums.OperateTypeEnum;
import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.user.UserRelationReq;
import com.example.forum.api.model.vo.user.dto.FollowUserInfoDTO;
import com.example.forum.service.user.entity.UserFootDO;
import com.example.forum.service.user.mapper.UserFootMapper;
import com.example.forum.service.user.service.UserFootService;
import com.example.forum.service.user.service.UserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserFootServiceImpl implements UserFootService {

    @Autowired
    private UserFootMapper userFootMapper;

    @Autowired
    private UserRelationService userRelationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "articleDetail", key = "#articleId")
    public void collect(Long articleId, Long userId) {
        UserFootDO foot = userFootMapper.findByUserIdAndDocument(userId, articleId, DocumentTypeEnum.ARTICLE.ordinal() + 1);
        if (foot == null) {
            foot = buildNewFoot(userId, articleId, DocumentTypeEnum.ARTICLE);
            foot.setCollectionStat(1);
            userFootMapper.insert(foot);
        } else {
            foot.setCollectionStat(1);
            userFootMapper.update(foot);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "articleDetail", key = "#articleId")
    public void cancelCollect(Long articleId, Long userId) {
        UserFootDO foot = userFootMapper.findByUserIdAndDocument(userId, articleId, DocumentTypeEnum.ARTICLE.ordinal() + 1);
        if (foot != null) {
            foot.setCollectionStat(2);
            userFootMapper.update(foot);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "articleDetail", key = "#articleId")
    public void praise(Long articleId, Long userId) {
        UserFootDO foot = userFootMapper.findByUserIdAndDocument(userId, articleId, DocumentTypeEnum.ARTICLE.ordinal() + 1);
        if (foot == null) {
            foot = buildNewFoot(userId, articleId, DocumentTypeEnum.ARTICLE);
            foot.setPraiseStat(1);
            userFootMapper.insert(foot);
        } else {
            foot.setPraiseStat(1);
            userFootMapper.update(foot);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "articleDetail", key = "#articleId")
    public void cancelPraise(Long articleId, Long userId) {
        UserFootDO foot = userFootMapper.findByUserIdAndDocument(userId, articleId, DocumentTypeEnum.ARTICLE.ordinal() + 1);
        if (foot != null) {
            foot.setPraiseStat(2);
            userFootMapper.update(foot);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void follow(UserRelationReq req, Long userId) {
        userRelationService.follow(userId, req.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelFollow(UserRelationReq req, Long userId) {
        userRelationService.unfollow(userId, req.getUserId());
    }

    @Override
    public List<FollowUserInfoDTO> getFollowList(Long userId, Integer page, Integer size) {
        PageParam pageParam = PageParam.newPageInstance(
                page != null ? page.longValue() : 1L,
                size != null ? size.longValue() : 10L);
        PageListVo<FollowUserInfoDTO> result = userRelationService.queryFollowList(userId, pageParam);
        return result.getList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "articleDetail", key = "#documentId", condition = "#documentType == T(com.example.forum.api.model.enums.DocumentTypeEnum).ARTICLE")
    public void favorArticleComment(DocumentTypeEnum documentType, Long documentId, Long authorUserId, Long loginUserId, OperateTypeEnum operate) {
        // document_type: 1-文章, 2-评论 (与数据库定义一册"
        int docType = documentType.ordinal() + 1;

        UserFootDO foot = userFootMapper.findByUserIdAndDocument(loginUserId, documentId, docType);
        if (foot == null) {
            foot = new UserFootDO();
            foot.setUserId(loginUserId);
            foot.setDocumentId(documentId);
            foot.setDocumentType(docType);
            foot.setDocumentUserId(authorUserId);
            foot.setReadStat(0);
            foot.setCollectionStat(0);
            foot.setCommentStat(0);
            foot.setPraiseStat(0);

            switch (operate) {
                case LIKE:
                    foot.setPraiseStat(1);
                    break;
                case COLLECT:
                    foot.setCollectionStat(1);
                    break;
                case CANCEL_LIKE:
                    foot.setPraiseStat(2);
                    break;
                case CANCEL_COLLECT:
                    foot.setCollectionStat(2);
                    break;
                default:
                    break;
            }
            userFootMapper.insert(foot);
        } else {
            switch (operate) {
                case LIKE:
                    foot.setPraiseStat(foot.getPraiseStat() == 1 ? 2 : 1);
                    break;
                case COLLECT:
                    foot.setCollectionStat(foot.getCollectionStat() == 1 ? 2 : 1);
                    break;
                case CANCEL_LIKE:
                    foot.setPraiseStat(2);
                    break;
                case CANCEL_COLLECT:
                    foot.setCollectionStat(2);
                    break;
                default:
                    break;
            }
            userFootMapper.update(foot);
        }
    }

    private UserFootDO buildNewFoot(Long userId, Long articleId, DocumentTypeEnum documentType) {
        UserFootDO foot = new UserFootDO();
        foot.setUserId(userId);
        foot.setDocumentId(articleId);
        foot.setDocumentType(documentType.ordinal() + 1);
        foot.setDocumentUserId(0L);
        foot.setReadStat(0);
        foot.setCollectionStat(0);
        foot.setCommentStat(0);
        foot.setPraiseStat(0);
        return foot;
    }
}

