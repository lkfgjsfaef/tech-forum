package com.example.forum.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleUtil {
    private static final Pattern IMG_PATTERN = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
    
    public static List<String> extractImages(String content) {
        List<String> images = new ArrayList<>();
        if (content == null) return images;
        Matcher matcher = IMG_PATTERN.matcher(content);
        while (matcher.find()) {
            images.add(matcher.group(1));
        }
        return images;
    }
    
    public static String extractFirstImage(String content) {
        List<String> images = extractImages(content);
        return images.isEmpty() ? null : images.get(0);
    }
    
    public static String generateSummary(String content, int length) {
        if (content == null) return "";
        String text = content.replaceAll("!\\[.*?\\]\\(.*?\\)", "")
                .replaceAll("\\[.*?\\]\\(.*?\\)", "")
                .replaceAll("#", "")
                .replaceAll("\\*", "")
                .replaceAll("`", "")
                .trim();
        if (text.length() <= length) return text;
        return text.substring(0, length) + "...";
    }
}
