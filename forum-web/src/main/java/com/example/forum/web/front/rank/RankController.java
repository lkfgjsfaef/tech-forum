package com.example.forum.web.front.rank;

import com.example.forum.api.model.enums.rank.ActivityRankTimeEnum;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.rank.dto.RankInfoDTO;
import com.example.forum.api.model.vo.rank.dto.RankItemDTO;
import com.example.forum.service.rank.service.UserActivityRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 排行册"
 *
 * @author dev
 * @date 2023/8/20
 */
@Controller
public class RankController {
    @Autowired
    private UserActivityRankService userActivityRankService;

    /**
     * 活跃用户排行榜页册"
     */
    @RequestMapping(path = "/rank/{time}")
    public String rank(@PathVariable(value = "time") String time, Model model,
                       @RequestParam(value = "ajax", required = false) String ajax) {
        ActivityRankTimeEnum rankTime = ActivityRankTimeEnum.nameOf(time);
        if (rankTime == null) {
            rankTime = ActivityRankTimeEnum.MONTH;
        }
        List<RankItemDTO> list = userActivityRankService.queryRankList(rankTime, 30);
        RankInfoDTO info = new RankInfoDTO();
        info.setItems(list);
        info.setTime(rankTime);
        model.addAttribute("vo", ResVo.ok(info));
        return "views/rank/index";
    }

    /**
     * 活跃用户排行册"AJAX 接口（供首页动态切换榜单）
     */
    @RequestMapping(path = "/rank/{time}", params = "ajax=true")
    @ResponseBody
    public ResVo<RankInfoDTO> rankAjax(@PathVariable(value = "time") String time) {
        ActivityRankTimeEnum rankTime = ActivityRankTimeEnum.nameOf(time);
        if (rankTime == null) {
            rankTime = ActivityRankTimeEnum.MONTH;
        }
        List<RankItemDTO> list = userActivityRankService.queryRankList(rankTime, 30);
        RankInfoDTO info = new RankInfoDTO();
        info.setItems(list);
        info.setTime(rankTime);
        return ResVo.ok(info);
    }
}

