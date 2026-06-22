package com.example.forum.web.admin.rest;

import com.example.forum.api.model.enums.PushStatusEnum;
import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.article.SearchTagReq;
import com.example.forum.api.model.vo.article.TagReq;
import com.example.forum.api.model.vo.article.dto.TagDTO;
import com.example.forum.api.model.vo.constants.StatusEnum;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.article.service.TagSettingService;
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
@RequestMapping(path = {"api/admin/tag/", "admin/tag/"})
public class TagSettingRestController {

    @Autowired
    private TagSettingService tagSettingService;

    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody TagReq req) {
        tagSettingService.saveTag(req);
        return ResVo.ok();
    }

    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResVo<String> delete(@RequestParam(name = "tagId") Integer tagId) {
        tagSettingService.deleteTag(tagId);
        return ResVo.ok();
    }

    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResVo<String> operate(@RequestParam(name = "tagId") Integer tagId,
                                 @RequestParam(name = "pushStatus") Integer pushStatus) {
        if (pushStatus != PushStatusEnum.OFFLINE.getCode() && pushStatus!= PushStatusEnum.ONLINE.getCode()) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        tagSettingService.operateTag(tagId, pushStatus);
        return ResVo.ok();
    }

    @PostMapping(path = "list")
    public ResVo<PageVo<TagDTO>> list(@RequestBody SearchTagReq req) {
        PageVo<TagDTO> tagDTOPageVo = tagSettingService.getTagList(req);
        return ResVo.ok(tagDTOPageVo);
    }
}

