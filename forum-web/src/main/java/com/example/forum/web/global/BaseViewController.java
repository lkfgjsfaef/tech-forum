package com.example.forum.web.global;

import com.example.forum.api.model.vo.PageParam;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 全局属性配册"
 *
 * @author dev
 * @date 2022/9/3
 */
public class BaseViewController {
    @Autowired
    protected GlobalInitService globalInitService;

    public PageParam buildPageParam(Long page, Long size) {
        if (page <= 0) {
            page = PageParam.DEFAULT_PAGE_NUM;
        }
        if (size == null || size > PageParam.DEFAULT_PAGE_SIZE) {
            size = PageParam.DEFAULT_PAGE_SIZE;
        }
        return PageParam.newPageInstance(page, size);
    }

//
//  推荐使用它替册"GlobalViewInterceptor 中的全局属性设册"
//    /**
//     * 全局属性配册"
//     *
//     * @param model
//     */
//    @ModelAttribute
//    public void globalAttr(Model model) {
//        model.addAttribute("global", globalInitService.globalAttr());
//    }
}

