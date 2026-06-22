package com.example.forum.api.model.context;

import com.example.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import com.example.forum.api.model.vo.seo.Seo;
import lombok.Data;

@Data
public class ReqInfoContext {
    private static final ThreadLocal<ReqInfo> REQ_INFO = new ThreadLocal<>();

    public static ReqInfo getReqInfo() {
        return REQ_INFO.get();
    }

    public static void setReqInfo(ReqInfo reqInfo) {
        REQ_INFO.set(reqInfo);
    }

    public static void addReqInfo(ReqInfo reqInfo) {
        REQ_INFO.set(reqInfo);
    }

    public static void clear() {
        REQ_INFO.remove();
    }

    @Data
    public static class ReqInfo {
        private String session;
        private Long userId;
        private BaseUserInfoDTO user;
        private Integer msgNum;
        private Seo seo;
        private String clientIp;
        private String referer;
        private String userAgent;
        private String host;
        private String path;
        private String deviceId;
        private String payload;
        private Long chatId;
    }
}
