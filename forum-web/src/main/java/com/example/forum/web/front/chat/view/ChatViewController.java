package com.example.forum.web.front.chat.view;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.chat.entity.ChatMessageDO;
import com.example.forum.service.chat.entity.ChatSessionDO;
import com.example.forum.service.chat.service.ChatService;
import com.example.forum.service.user.service.UserService;
import com.example.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping(path = "message")
public class ChatViewController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Permission(role = UserRole.LOGIN)
    @GetMapping
    public String messagePage(@RequestParam(name = "targetUserId", required = false) Long targetUserId,
                               Model model) {
        Long currentUserId = ReqInfoContext.getReqInfo().getUserId();
        List<ChatSessionDO> sessions = chatService.getUserSessions(currentUserId);
        model.addAttribute("sessions", sessions);
        model.addAttribute("currentUserId", currentUserId);

        // 为每个会话补充对方用户名
        Map<Long, String> sessionOtherName = new HashMap<>();
        Map<Long, String> sessionOtherPhoto = new HashMap<>();
        for (ChatSessionDO sess : sessions) {
            Long otherId = sess.getUser1Id().equals(currentUserId) ? sess.getUser2Id() : sess.getUser1Id();
            UserStatisticInfoDTO otherUser = userService.queryUserInfoWithStatistic(otherId);
            if (otherUser != null) {
                sessionOtherName.put(sess.getId(), otherUser.getUserName());
                sessionOtherPhoto.put(sess.getId(), otherUser.getPhoto());
            } else {
                sessionOtherName.put(sess.getId(), "用户" + otherId);
                sessionOtherPhoto.put(sess.getId(), null);
            }
        }
        model.addAttribute("sessionOtherName", sessionOtherName);
        model.addAttribute("sessionOtherPhoto", sessionOtherPhoto);

        if (targetUserId != null) {
            model.addAttribute("targetUserId", targetUserId);
            UserStatisticInfoDTO targetUser = userService.queryUserInfoWithStatistic(targetUserId);
            if (targetUser != null) {
                model.addAttribute("targetUserName", targetUser.getUserName());
                model.addAttribute("targetUserPhoto", targetUser.getPhoto());
            }
        }

        model.addAttribute("unreadCount", chatService.getUnreadCount(currentUserId));
        return "views/chat-message/index";
    }
}
