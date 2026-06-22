package com.example.forum.web.admin.rest;

import com.example.forum.api.model.enums.PushStatusEnum;
import com.example.forum.api.model.vo.PageVo;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.article.CategoryReq;
import com.example.forum.api.model.vo.article.SearchCategoryReq;
import com.example.forum.api.model.vo.article.dto.CategoryDTO;
import com.example.forum.api.model.vo.constants.StatusEnum;
import com.example.forum.core.permission.Permission;
import com.example.forum.core.permission.UserRole;
import com.example.forum.service.article.service.CategorySettingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类后台
 *
 * @author dev
 * @date 2022/9/19
 */
@RestController
@Permission(role = UserRole.LOGIN)
@Tag(name = "API")
@RequestMapping(path = {"api/admin/category/", "admin/category/"})
public class CategorySettingRestController {

    @Autowired
    private CategorySettingService categorySettingService;


    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody CategoryReq req) {
        categorySettingService.saveCategory(req);
        return ResVo.ok();
    }


    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResVo<String> delete(@RequestParam(name = "categoryId") Integer categoryId) {
        categorySettingService.deleteCategory(categoryId);
        return ResVo.ok();
    }


    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResVo<String> operate(@RequestParam(name = "categoryId") Integer categoryId,
                                 @RequestParam(name = "pushStatus") Integer pushStatus) {
        if (pushStatus != PushStatusEnum.OFFLINE.getCode() && pushStatus!= PushStatusEnum.ONLINE.getCode()) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        categorySettingService.operateCategory(categoryId, pushStatus);
        return ResVo.ok();
    }


    @PostMapping(path = "list")
    public ResVo<PageVo<CategoryDTO>> list(@RequestBody SearchCategoryReq req) {
        PageVo<CategoryDTO> categoryDTOPageVo = categorySettingService.getCategoryList(req);
        return ResVo.ok(categoryDTOPageVo);
    }
}

