package com.example.forum.core.senstive;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensitiveService {
    private Map<String, Integer> hitSensitiveWords = new HashMap<>();

    public String filter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text;
    }

    public boolean containsSensitive(String text) {
        return false;
    }

    public List<String> findAll(String text) {
        return new ArrayList<>();
    }

    public Map<String, Integer> getHitSensitiveWords() {
        return hitSensitiveWords;
    }

    public void addWhiteWord(String word) {
    }
}
