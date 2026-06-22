package com.example.forum.service.notify.service;

import com.example.forum.api.model.enums.NotifyTypeEnum;
import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.notify.dto.NotifyMsgDTO;

import java.util.Map;

public interface NotifyService {
    String NOTIFY_TOPIC = "/topic/notify";

    int getUnreadCount(Long userId);
    void markAsRead(Long notifyId);
    void markAllAsRead(Long userId);
    int queryUserNotifyMsgCount(Long userId);
    void notifyToUser(Long userId, String message);
    PageListVo<NotifyMsgDTO> queryUserNotices(Long userId, NotifyTypeEnum typeEnum, PageParam pageParam);
    void notifyChannelMaintain(Object accessor);
    Map<String, Integer> queryUnreadCounts(Long userId);
}
