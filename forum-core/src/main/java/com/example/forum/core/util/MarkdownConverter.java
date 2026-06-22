package com.example.forum.core.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

/**
 * Markdown转换为HTML的工具类
 * 使用Flexmark库实册"
 */
public class MarkdownConverter {

    private static final Parser PARSER;
    private static final HtmlRenderer RENDERER;

    static {
        MutableDataSet options = new MutableDataSet();
        PARSER = Parser.builder(options).build();
        RENDERER = HtmlRenderer.builder(options).build();
    }

    /**
     * Markdown转HTML
     *
     * @param markdown Markdown原文
     * @return HTML字符串"
     */
    public static String markdownToHtml(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        try {
            return RENDERER.render(PARSER.parse(markdown));
        } catch (Exception e) {
            // 如果转换失败，返回原文"
            return markdown;
        }
    }

    /**
     * 兼容旧方法"
     */
    public static String toHtml(String markdown) {
        return markdownToHtml(markdown);
    }

    /**
     * HTML转Markdown（暂不支持）
     */
    public static String toMarkdown(String html) {
        if (html == null) {
            return "";
        }
        // 目前不实现反向转换"
        return html;
    }
}


