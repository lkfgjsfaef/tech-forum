package com.example.forum.api.model.vo.user.dto;

import lombok.Data;

@Data
public class UserPayCodeDTO {
    private String payCode;
    private String qrCodeUrl;
}
