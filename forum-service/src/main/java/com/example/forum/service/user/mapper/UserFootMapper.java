package com.example.forum.service.user.mapper;

import com.example.forum.service.user.entity.UserFootDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserFootMapper {
    void insert(UserFootDO userFoot);

    void update(UserFootDO userFoot);

    UserFootDO findByUserIdAndDocument(@Param("userId") Long userId,
                                       @Param("documentId") Long documentId,
                                       @Param("documentType") Integer documentType);

    /**
     * 统计文档的点赞数
     */
    int countPraise(@Param("documentId") Long documentId,
                    @Param("documentType") Integer documentType);

    /**
     * 统计文档的收藏数
     */
    int countCollection(@Param("documentId") Long documentId,
                        @Param("documentType") Integer documentType);

    /**
     * 统计用户的点赞文章数
     */
    int countUserPraise(@Param("userId") Long userId,
                        @Param("documentType") Integer documentType);

    /**
     * 统计用户的收藏文章数
     */
    int countUserCollection(@Param("userId") Long userId,
                            @Param("documentType") Integer documentType);

    /**
     * 统计用户收到的点赞数（别人点赞用户发布的文档册"
     */
    int countReceivedPraise(@Param("documentUserId") Long documentUserId,
                            @Param("documentType") Integer documentType);
}

