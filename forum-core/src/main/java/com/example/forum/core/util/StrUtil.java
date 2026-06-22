package com.example.forum.core.util;

public class StrUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    public static String safeSubstringHtml(String html, int length) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        if (length <= 0 || html.length() <= length) {
            return html;
        }
        String result = html.substring(0, length);
        int lastOpenTag = result.lastIndexOf('<');
        int lastCloseTag = result.lastIndexOf('>');
        if (lastOpenTag > lastCloseTag) {
            result = result.substring(0, lastOpenTag);
        }
        return result + "...";
    }
}
