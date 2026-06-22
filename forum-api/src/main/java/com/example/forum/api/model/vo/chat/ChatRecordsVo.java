package com.example.forum.api.model.vo.chat;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ChatRecordsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String chatId;
    private List<ChatRecordVo> records;
    private boolean hasMore;

    @Data
    public static class ChatRecordVo implements Serializable {
        private static final long serialVersionUID = 1L;

        private String question;
        private String answer;
        private Long timestamp;
    }
}
