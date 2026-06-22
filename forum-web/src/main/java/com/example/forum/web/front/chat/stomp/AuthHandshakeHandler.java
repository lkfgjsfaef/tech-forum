package com.example.forum.web.front.chat.stomp;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.service.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * 握手处理册"
 *
 * @author dev
 * @date 2023/6/8
 */
@Slf4j
public class AuthHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // case1: 根据cookie来识别用户，即可以实现所有用户连相同的ws地址，然后再 AuthHandshakeChannelInterceptor 中进行destination的转换"
        ReqInfoContext.ReqInfo reqInfo = (ReqInfoContext.ReqInfo) attributes.get(LoginService.SESSION_KEY);
        if (reqInfo != null) {
            final Long userId = reqInfo.getUserId();
            return () -> String.valueOf(userId);
        }

        // case2: 根据路径来区分用册"
        // 获取例如 ws://localhost/gpt/id 订阅地址中的最后一个用册"id 参数作为用户的标册" 为实现发送信息给指定用户做准册"
        String uri = request.getURI().toString();
        String uid = uri.substring(uri.lastIndexOf("/") + 1);
        log.info("{} -> {}", uri, uid);
        return () -> uid;
    }
}


