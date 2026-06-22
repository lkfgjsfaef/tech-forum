package com.example.forum.web.common;

import com.example.forum.api.model.vo.ResVo;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.config.service.DictCommonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 通用
 *
 * @author dev
 * @date 2022/9/19
 */
@RestController
@Slf4j
@Permission(role = UserRole.LOGIN)
@Tag(name = "API")
@RequestMapping(path = {"common/","api/admin/common/", "admin/common/"})
public class DictCommonController {

    @Autowired
    private DictCommonService dictCommonService;

    @ResponseBody
    @GetMapping(path = "/dict")
    public ResVo<Map<String, Object>> list() {
        log.debug("获取字典");
        Map<String, Object> bannerDTOPageVo = dictCommonService.getDict();
        return ResVo.ok(bannerDTOPageVo);
    }
}

