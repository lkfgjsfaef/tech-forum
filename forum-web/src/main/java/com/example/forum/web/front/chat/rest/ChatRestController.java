package com.example.forum.web.front.chat.rest;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.constants.StatusEnum;
import com.example.forum.service.chat.entity.ChatMessageDO;
import com.example.forum.service.chat.entity.ChatSessionDO;
import com.example.forum.service.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "chat/api")
public class ChatRestController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/sessions")
    public ResVo<List<ChatSessionDO>> getSessions() {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        List<ChatSessionDO> sessions = chatService.getUserSessions(userId);
        return ResVo.ok(sessions);
    }

    @GetMapping("/messages")
    public ResVo<Map<String, Object>> getMessages(@RequestParam Long sessionId,
                                                    @RequestParam(defaultValue = "1") int pageNum,
                                                    @RequestParam(defaultValue = "50") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        result.put("messages", chatService.getSessionMessages(sessionId, pageNum, pageSize).getList());
        return ResVo.ok(result);
    }

    @PostMapping("/send")
    public ResVo<ChatMessageDO> sendMessage(@RequestBody Map<String, Object> body) {
        Long fromUserId = ReqInfoContext.getReqInfo().getUserId();
        Long toUserId = Long.valueOf(body.get("toUserId").toString());
        String content = body.get("content").toString();
        ChatMessageDO message = chatService.sendMessage(fromUserId, toUserId, content);
        if (message == null) {
            return ResVo.fail(StatusEnum.FAIL, "消息内容不能为空");
        }
        return ResVo.ok(message);
    }

    @PostMapping("/read")
    public ResVo<Void> markRead(@RequestBody Map<String, Object> body) {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        Long sessionId = Long.valueOf(body.get("sessionId").toString());
        chatService.markSessionAsRead(sessionId, userId);
        return ResVo.ok(null);
    }

    @GetMapping("/unread")
    public ResVo<Integer> unreadCount() {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        return ResVo.ok(chatService.getUnreadCount(userId));
    }
}
