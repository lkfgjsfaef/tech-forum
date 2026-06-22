package com.example.forum.service.statistics.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("request_count")
public class RequestCountDO {

    private Long id;
    private Integer cnt;
    private Date createTime;
}
