package com.niit.agent.controller;

import com.niit.agent.common.result.Result;
import com.niit.agent.entity.ChatAttachment;
import com.niit.agent.service.ChatAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final ChatAttachmentService chatAttachmentService;

    @PostMapping("/file")
    public Result<ChatAttachment> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "sessionId", required = false) Long sessionId) {
        try {
            if (file.isEmpty()) {
                return Result.fail("文件不能为空");
            }
            // limit size 10MB
            if (file.getSize() > 10 * 1024 * 1024) {
                return Result.fail("文件大小不能超过 10MB");
            }
            ChatAttachment attachment = chatAttachmentService.uploadAndParse(file, sessionId);
            return Result.ok(attachment);
        } catch (Exception e) {
            return Result.fail("文件上传或解析失败" " + e.getMessage());
        }
    }
}


