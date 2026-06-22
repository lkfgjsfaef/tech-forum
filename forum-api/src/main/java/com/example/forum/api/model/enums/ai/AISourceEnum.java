package com.example.forum.api.model.enums.ai;

import lombok.Getter;

@Getter
public enum AISourceEnum {
    CHAT_GPT("ChatGPT"),
    CHAT_GPT_3_5("ChatGPT 3.5"),
    CHAT_GPT_4("ChatGPT 4"),
    GLM_46V("GLM-4.6V"),
    GLM_45AIR("GLM-4.5-Air"),
    GLM_47("GLM-4.7"),
    ALI_AI("通义千问"),
    XUN_FEI_AI("讯飞星火"),
    ZHI_PU_AI("智谱"),
    ZHIPU_CODING("智谱Coding"),
    DOU_BAO_AI("豆包"),
    DEEP_SEEK("DeepSeek"),
    PAI_AI("Tech Forum"),
    TONG_YI("通义千问"),
    WEN_XIN("文心一言"),
    NONE("未启册");
    
    private final String desc;
    private final String code;
    
    AISourceEnum(String desc) {
        this.desc = desc;
        this.code = this.name();
    }
    
    AISourceEnum(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }
    
    public String getName() {
        return this.desc;
    }
}

