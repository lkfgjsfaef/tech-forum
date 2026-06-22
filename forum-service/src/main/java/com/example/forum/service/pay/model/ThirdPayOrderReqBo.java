package com.example.forum.service.pay.model;

import com.example.forum.api.model.enums.pay.ThirdPayWayEnum;
import lombok.Data;

@Data
public class ThirdPayOrderReqBo {

    private String outTradeNo;
    private String description;
    private Integer total;
    private ThirdPayWayEnum payWay;
}
