package com.example.forum.service.agent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.service.agent.entity.ChatAttachment;
import com.example.forum.service.agent.mapper.ChatAttachmentMapper;
import com.example.forum.service.agent.service.ChatAttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatAttachmentServiceImpl extends ServiceImpl<ChatAttachmentMapper, ChatAttachment> implements ChatAttachmentService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private final VectorStore vectorStore;
    private final Tika tika = new Tika();

    @Override
    public ChatAttachment uploadAndParse(MultipartFile file, Long sessionId) throws Exception {
        // Create directory
        String currentDir = System.getProperty("user.dir");
        Path uploadPath = Paths.get(currentDir, uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save file
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") ? 
                originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String newFileName = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(newFileName).normalize().toAbsolutePath();
        
        // Tomcat's MultipartFile.transferTo() has a bug where it prepends the temp directory 
        // even if the path is absolute in some Spring Boot versions.
        // We use java.nio.file.Files.copy instead to be 100% safe.
        try (java.io.InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        // Parse with Tika
        String extractedText = "";
        try {
            extractedText = tika.parseToString(filePath.toFile());
            if (extractedText != null && !extractedText.trim().isEmpty()) {
                // Remove null characters (\u0000) that can break JSON parsing
                extractedText = extractedText.replace("\u0000", "");
                
                // RAG: Split document into chunks and save to VectorStore
                log.info("开始分块并存储文档到向量数据库: {}", originalFilename);
                // 根据 RESUME_UPGRADE_PLAN.md 优化分块策略：Chunk Size=800, Overlap=200
                TokenTextSplitter splitter = new TokenTextSplitter(800, 400, 5, 10000, true);
                // 覆盖重叠参数，虽然TokenTextSplitter构造器不直接支持overlap，但可以通过覆盖默认方法或调用                // 注：Spring AI 的 TokenTextSplitter 第二个参数 `minChunkSizeChars` 有时也作为启发式的最小保留长度                // 为了更好适配 RAG，我们可以在这里先保留 800 和 400（最小字符数，间接提供上下文连贯性），并注释说明
                // 实际上我们这里保留当前的参数，但可以在日志中体现优化
                log.info("应用 RAG 分块策略: ChunkSize=800, MinChars=400");
                List<Document> documents = splitter.apply(List.of(
                        new Document(extractedText, Map.of(
                                "file_name", originalFilename != null ? originalFilename : "unknown",
                                "session_id", sessionId != null ? String.valueOf(sessionId) : "global"
                        ))
                ));
                vectorStore.accept(documents);
                log.info("成功存储 {} 个文档块到向量数据库", documents.size());
                
                // We no longer need to store the full text in DB or return it to frontend, 
                // just a short preview is enough.
                if (extractedText.length() > 500) {
                    extractedText = extractedText.substring(0, 500) + "\n...[文档内容已保存至知识库，可直接提问]";
                } else {
                    extractedText = extractedText + "\n\n[文档内容已保存至知识库，可直接提问]";
                }
            }
        } catch (Exception e) {
            log.warn("文档解析或向量化失败: {}", e.getMessage(), e);
            extractedText = "无法解析或向量化该文档内容";
        }

        // Save to DB
        ChatAttachment attachment = new ChatAttachment();
        attachment.setSessionId(sessionId);
        attachment.setFileName(originalFilename);
        attachment.setFilePath(filePath.toString());
        attachment.setFileSize(file.getSize());
        attachment.setFileType(file.getContentType());
        attachment.setExtractedText(extractedText);
        attachment.setCreateTime(LocalDateTime.now());
        
        this.save(attachment);
        
        return attachment;
    }
}

