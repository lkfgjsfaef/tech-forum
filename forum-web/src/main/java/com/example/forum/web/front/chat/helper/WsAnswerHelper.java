package com.example.forum.web.front.chat.helper;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.api.model.enums.ai.AISourceEnum;
import com.example.forum.api.model.vo.chat.ChatRecordsVo;
import com.example.forum.core.mdc.MdcUtil;
import com.example.forum.core.ws.WebSocketResponseUtil;
import com.example.forum.service.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dev
 * @date 2023/6/9
 */
@Slf4j
@Component
public class WsAnswerHelper {
    public static final String AI_SOURCE_PARAM = "AI";

    private void sendMsgToUser(String session, String question) {
        // AI功能已迁移到 Agent 模块，旧版 ChatFacade 已废弃
        log.info("AI直接返回：{}", question);
    }

    public void sendMsgToUser(AISourceEnum ai, String session, String question) {
        if (ai == null) {
            // 自动选择AI类型
            sendMsgToUser(session, question);
        } else {
            // AI功能已迁移到 Agent 模块，旧版 ChatFacade 已废弃
            log.info("AI直接返回：{}", question);
        }
    }

    public void sendMsgHistoryToUser(String session, AISourceEnum ai) {
        // AI功能已迁移到 Agent 模块，旧版 ChatFacade 已废弃
    }

    /**
     * 将返回结果推送给用户
     *
     * @param session
     * @param response
     */
    public void response(String session, ChatRecordsVo response) {
        // convertAndSendToUser 方法可以发送信给给指定用户,
        // 底层会自动将第二个参数目的地址 /chat/rsp 拼接
        // /user/username/chat/rsp，其中第二个参数 username 即为这里的第一个参数session
        // username 也是AuthHandshakeHandler中配置的 Principal 用户识别标志
        WebSocketResponseUtil.sendMsgToUser(session, "/chat/rsp", response);
    }

    public void execute(Map<String, Object> attributes, Runnable func) {
        try {
            ReqInfoContext.ReqInfo reqInfo = (ReqInfoContext.ReqInfo) attributes.get(LoginService.SESSION_KEY);
            ReqInfoContext.addReqInfo(reqInfo);
            String traceId = (String) attributes.get(MdcUtil.TRACE_ID_KEY);
            MdcUtil.add(MdcUtil.TRACE_ID_KEY, traceId);


            // 执行具体的业务逻辑
            func.run();

        } finally {
            ReqInfoContext.clear();
            MdcUtil.clear();
        }
    }
}

