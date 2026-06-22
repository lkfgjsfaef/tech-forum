package com.example.forum.service.statistics.converter;

import com.example.forum.service.statistics.repository.entity.RequestCountDO;
import com.example.forum.service.statistics.repository.entity.RequestCountExcelDO;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticsConverter {

    public static List<RequestCountExcelDO> convertToRequestCountExcelDOList(List<RequestCountDO> data) {
        return data.stream().map(StatisticsConverter::convertToRequestCountExcelDO).collect(Collectors.toList());
    }

    public static RequestCountExcelDO convertToRequestCountExcelDO(RequestCountDO data) {
        RequestCountExcelDO excelDO = new RequestCountExcelDO();
        excelDO.setId(data.getId());
        excelDO.setCnt(data.getCnt());
        excelDO.setCreateTime(data.getCreateTime());
        return excelDO;
    }
}
