package com.example.forum.api.model.enums.pay;

import lombok.Getter;

@Getter
public enum ThirdPayWayEnum {
    WECHAT("微信支付"),
    WX_H5("微信H5支付"),
    WX_NATIVE("微信Native支付"),
    ALIPAY("支付册"),
    EMAIL("邮箱支付"),
    OFFLINE("线下支付"),
    NONE("未支册");
    
    private final String desc;
    
    ThirdPayWayEnum(String desc) {
        this.desc = desc;
    }
}

