package com.example.forum.core.util;

import java.util.regex.Pattern;

public class XssUtil {

    private static final Pattern SCRIPT_PATTERN = Pattern.compile(
            "<script[^>]*>.*?</script>",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    private static final Pattern EVENT_HANDLER_PATTERN = Pattern.compile(
            "on\\w+\\s*=\\s*['\"]?[^'\"\\s]*['\"]?",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern JS_PROTOCOL_PATTERN = Pattern.compile(
            "javascript\\s*:|vbscript\\s*:|data\\s*:",
            Pattern.CASE_INSENSITIVE
    );

    public static String clean(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String result = input;

        result = SCRIPT_PATTERN.matcher(result).replaceAll("");
        result = EVENT_HANDLER_PATTERN.matcher(result).replaceAll("");
        result = JS_PROTOCOL_PATTERN.matcher(result).replaceAll("");

        result = result.replace("<", "&lt;")
                      .replace(">", "&gt;");

        return result;
    }

    public static boolean containsXss(String input) {
        if (input == null) return false;
        return SCRIPT_PATTERN.matcher(input).find()
                || EVENT_HANDLER_PATTERN.matcher(input).find()
                || JS_PROTOCOL_PATTERN.matcher(input).find();
    }

    public static String escapeHtml(String input) {
        if (input == null) return null;
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }
}
