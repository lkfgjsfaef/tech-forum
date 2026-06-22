package com.example.forum.core.dal;

public enum MasterSlaveDsEnum {
    MASTER("master", "主库"),
    SLAVE("slave", "从库");

    private final String code;
    private final String desc;

    MasterSlaveDsEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
