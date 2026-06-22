package com.example.forum.web.admin.rest;

import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.config.SensitiveWordConfigReq;
import com.example.forum.api.model.vo.config.SearchSensitiveWordHitReq;
import com.example.forum.api.model.vo.config.SensitiveWordOperateReq;
import com.example.forum.api.model.vo.config.dto.SensitiveWordConfigDTO;
import com.example.forum.api.model.vo.config.dto.SensitiveWordHitDTO;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.sensitive.service.SensitiveAdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 敏感词后册"
 *
 * @author dev
 * @date 2026/3/24
 */
@RestController
@Permission(role = UserRole.LOGIN)
@Tag(name = "API")
@RequestMapping(path = {"/api/admin/sensitive/", "/admin/sensitive/"})
public class SensitiveWordAdminRestController {

    @Autowired
    private SensitiveAdminService sensitiveAdminService;

    @Permission(role = UserRole.ADMIN)
    @Operation(summary = "获取敏感词配置详册")
    @GetMapping(path = "detail")
    public ResVo<SensitiveWordConfigDTO> detail() {
        return ResVo.ok(sensitiveAdminService.getConfig());
    }

    @Permission(role = UserRole.ADMIN)
    @Operation(summary = "分页获取敏感词命中统册")
    @PostMapping(path = "hit/list")
    public ResVo<PageVo<SensitiveWordHitDTO>> hitList(@RequestBody SearchSensitiveWordHitReq req) {
        return ResVo.ok(sensitiveAdminService.getHitWordPage(req));
    }

    @Permission(role = UserRole.ADMIN)
    @Operation(summary = "保存敏感词配册")
    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody SensitiveWordConfigReq req) {
        sensitiveAdminService.saveConfig(req);
        return ResVo.ok();
    }

    @Permission(role = UserRole.ADMIN)
    @Operation(summary = "清除敏感词命中统册")
    @PostMapping(path = "hit/clear")
    public ResVo<String> clearHitWord(@RequestBody SensitiveWordOperateReq req) {
        sensitiveAdminService.clearHitWord(req.getWord());
        return ResVo.ok();
    }
}

