package com.example.forum.service.notify.service.impl;

import com.example.forum.api.model.enums.NotifyTypeEnum;
import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.api.model.vo.PageParam;
import com.example.forum.api.model.vo.notify.dto.NotifyMsgDTO;
import com.example.forum.service.notify.service.NotifyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotifyServiceImpl implements NotifyService {

    @Override
    public int getUnreadCount(Long userId) {
        return 0;
    }

    @Override
    public void markAsRead(Long notifyId) {
    }

    @Override
    public void markAllAsRead(Long userId) {
    }

    @Override
    public int queryUserNotifyMsgCount(Long userId) {
        return 0;
    }

    @Override
    public void notifyToUser(Long userId, String message) {
    }

    @Override
    public PageListVo<NotifyMsgDTO> queryUserNotices(Long userId, NotifyTypeEnum typeEnum, PageParam pageParam) {
        PageListVo<NotifyMsgDTO> result = new PageListVo<>();
        result.setList(new ArrayList<>());
        result.setHasMore(false);
        return result;
    }

    @Override
    public void notifyChannelMaintain(Object accessor) {
    }

    @Override
    public Map<String, Integer> queryUnreadCounts(Long userId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("comment", 0);
        map.put("reply", 0);
        map.put("like", 0);
        map.put("collect", 0);
        map.put("follow", 0);
        map.put("system", 0);
        return map;
    }
}
