package com.example.forum.web.admin.rest;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.config.GlobalConfigReq;
import com.example.forum.api.model.vo.config.SearchGlobalConfigReq;
import com.example.forum.api.model.vo.config.dto.GlobalConfigDTO;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.config.service.GlobalConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 标签后台
 *
 * @author dev
 * @date 2022/9/19
 */
@RestController
@Permission(role = UserRole.LOGIN)
@Tag(name = "API")
@RequestMapping(path = {"api/admin/global/config/", "admin/global/config/"})
public class GlobalConfigRestController {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody GlobalConfigReq req) {
        globalConfigService.save(req);
        return ResVo.ok();
    }

    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResVo<String> delete(@RequestParam(name = "id") Long id) {
        globalConfigService.delete(id);
        return ResVo.ok();
    }

    @PostMapping(path = "list")
    @Permission(role = UserRole.ADMIN)
    public ResVo<PageVo<GlobalConfigDTO>> list(@RequestBody SearchGlobalConfigReq req) {
        PageVo<GlobalConfigDTO> page = globalConfigService.getList(req);
        return ResVo.ok(page);
    }
}

