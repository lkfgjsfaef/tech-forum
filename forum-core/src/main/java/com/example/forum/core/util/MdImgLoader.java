package com.example.forum.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MdImgLoader {
    private static final Pattern IMG_PATTERN = Pattern.compile("!\\[([^\\]]*)\\]\\(([^)]+)\\)");
    
    public static List<MdImg> extractImages(String content) {
        List<MdImg> images = new ArrayList<>();
        if (content == null) return images;
        Matcher matcher = IMG_PATTERN.matcher(content);
        while (matcher.find()) {
            MdImg img = new MdImg();
            img.setAlt(matcher.group(1));
            img.setUrl(matcher.group(2));
            images.add(img);
        }
        return images;
    }
    
    public static class MdImg {
        private String alt;
        private String url;
        
        public String getAlt() { return alt; }
        public void setAlt(String alt) { this.alt = alt; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }
}
