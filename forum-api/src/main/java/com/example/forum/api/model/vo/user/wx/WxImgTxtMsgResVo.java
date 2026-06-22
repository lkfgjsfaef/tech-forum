package com.example.forum.api.model.vo.user.wx;

import lombok.Data;
import java.util.List;

@Data
public class WxImgTxtMsgResVo {
    private List<WxImgTxtItemVo> articles;
}
