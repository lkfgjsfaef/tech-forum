package com.example.forum.web.admin.rest;

import com.example.forum.api.model.enums.user.UserAIStatEnum;
import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.user.SearchZsxqUserReq;
import com.example.forum.api.model.vo.user.ZsxqUserBatchOperateReq;
import com.example.forum.api.model.vo.user.ZsxqUserPostReq;
import com.example.forum.api.model.vo.user.dto.ZsxqUserInfoDTO;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.user.service.ZsxqWhiteListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 作者白名单服务
 *
 * @author dev
 * @date 2023/4/9
 */
@RestController
@Tag(name = "API")
@Permission(role = UserRole.ADMIN)
@RequestMapping(path = {"api/admin/zsxq/whitelist"})
public class ZsxqWhiteListController {
    @Autowired
    private ZsxqWhiteListService zsxqWhiteListService;

    @Operation(summary = "获取知识星球白名单用户列册")
    @PostMapping(path = "")
    public ResVo<PageVo<ZsxqUserInfoDTO>> list(@RequestBody SearchZsxqUserReq req) {
        PageVo<ZsxqUserInfoDTO> articleDTOPageVo = zsxqWhiteListService.getList(req);
        return ResVo.ok(articleDTOPageVo);
    }

    @Operation(summary = "改变用户状册")
    @GetMapping(path = "operate")
    public ResVo<String> operate(@RequestParam(name = "id") Long id,
                                  @RequestParam(name = "status") Integer status) {
        UserAIStatEnum operate = UserAIStatEnum.fromCode(status);
        zsxqWhiteListService.operate(id, operate);
        return ResVo.ok();
    }

    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "reset")
    public ResVo<String> reset(@RequestParam(name = "authorId") Integer authorId) {
        zsxqWhiteListService.reset(authorId);
        return ResVo.ok();
    }

    @Operation(summary = "批量审核通过")
    @PostMapping(path = "batchOperate")
    public ResVo<String> batchOperate(@RequestBody ZsxqUserBatchOperateReq req) {
        UserAIStatEnum operate = UserAIStatEnum.fromCode(req.getStatus());
        zsxqWhiteListService.batchOperate(req.getIds(), operate);
        return ResVo.ok();
    }

    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody ZsxqUserPostReq req) {
        zsxqWhiteListService.update(req);
        return ResVo.ok();
    }
}

