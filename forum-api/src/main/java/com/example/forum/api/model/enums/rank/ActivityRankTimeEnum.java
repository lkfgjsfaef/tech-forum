package com.example.forum.api.model.enums.rank;

import lombok.Getter;

@Getter
public enum ActivityRankTimeEnum {
    DAY("日榜"),
    WEEK("周榜"),
    MONTH("月榜"),
    ALL("总榜");
    
    private final String desc;
    
    ActivityRankTimeEnum(String desc) {
        this.desc = desc;
    }

    public static ActivityRankTimeEnum nameOf(String name) {
        if (name == null) {
            return null;
        }
        for (ActivityRankTimeEnum e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
