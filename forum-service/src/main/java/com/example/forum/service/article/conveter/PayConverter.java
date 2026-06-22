package com.example.forum.service.article.conveter;

import com.example.forum.api.model.vo.article.dto.ArticlePayInfoDTO;
import com.example.forum.api.model.vo.user.dto.PayCodeDTO;
import com.example.forum.api.model.vo.user.dto.UserPayCodeDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PayConverter {
    public static ArticlePayInfoDTO toPayInfoDTO(Object payInfo) {
        if (payInfo == null) return null;
        ArticlePayInfoDTO dto = new ArticlePayInfoDTO();
        dto.setPayed(false);
        dto.setPayCode("");
        dto.setPayAmount(0);
        return dto;
    }

    public static String genQrCode(String content) {
        if (content == null) return "";
        return "data:image/png;base64," + content;
    }

    public static List<PayCodeDTO> formatPayCodeInfo(String payCode) {
        if (payCode == null || payCode.isEmpty()) {
            return new ArrayList<>();
        }
        List<PayCodeDTO> result = new ArrayList<>();
        PayCodeDTO dto = new PayCodeDTO();
        dto.setCode(payCode);
        dto.setType("default");
        result.add(dto);
        return result;
    }

    public static Map<String, UserPayCodeDTO> formatPayCodeInfoAsMap(String payCode) {
        Map<String, UserPayCodeDTO> map = new LinkedHashMap<>();
        if (payCode == null || payCode.isEmpty()) {
            return map;
        }
        UserPayCodeDTO dto = new UserPayCodeDTO();
        dto.setPayCode(payCode);
        dto.setQrCodeUrl("data:image/png;base64," + payCode);
        map.put("default", dto);
        return map;
    }
}
