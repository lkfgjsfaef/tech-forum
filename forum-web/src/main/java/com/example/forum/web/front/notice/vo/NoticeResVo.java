package com.example.forum.web.front.notice.vo;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.notify.dto.NotifyMsgDTO;
import lombok.Data;

import java.util.Map;

/**
 * @author dev
 * @date 2022/9/4
 */
@Data
public class NoticeResVo {
    /**
     * 消息通知列表
     */
    private PageListVo<NotifyMsgDTO> list;

    /**
     * 每个分类的未读数册"
     */
    private Map<String, Integer> unreadCountMap;

    /**
     * 当前选中的消息类册"
     */
    private String selectType;
}

