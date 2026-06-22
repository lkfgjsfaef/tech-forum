package com.example.forum.service.agent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.service.agent.entity.ChatAttachment;
import org.springframework.web.multipart.MultipartFile;

public interface ChatAttachmentService extends IService<ChatAttachment> {
    ChatAttachment uploadAndParse(MultipartFile file, Long sessionId) throws Exception;
}
