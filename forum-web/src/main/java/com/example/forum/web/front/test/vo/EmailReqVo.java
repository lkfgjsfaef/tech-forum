package com.example.forum.web.front.test.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 邮件发送验册"
 *
 * @author dev
 * @date 2023/3/19
 */
@Data
public class EmailReqVo implements Serializable {
    private static final long serialVersionUID = -8560585303684975482L;

    private String to;

    private String title;

    private String content;

}

