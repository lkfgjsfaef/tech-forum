package com.example.forum.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.example.forum.core.util.SpringUtil;

@Slf4j
public class EmailUtil {
    private static JavaMailSender mailSender;

    public static void sendSimpleEmail(String to, String subject, String content) {
        try {
            if (mailSender == null) {
                mailSender = SpringUtil.getBean(JavaMailSender.class);
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("邮件发送成功, to={}", to);
        } catch (Exception e) {
            log.error("邮件发送失败, to={}", to, e);
        }
    }
}


