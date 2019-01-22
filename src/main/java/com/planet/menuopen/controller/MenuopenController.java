package com.planet.menuopen.controller;

import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.menuopen.domain.Menuopen;
import com.planet.menuopen.service.MenuopenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 佛祖保佑        永无BUG
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 *
 * @Author:liwufeng @Date:12:25 2018/12/9
 * @Description: Modified by:
 */
@Controller
@RequestMapping("/menuopen")
public class MenuopenController {
    private static final Logger log = LoggerFactory.getLogger(MenuopenController.class);

    @Autowired
    protected MenuopenService menuopenService;

    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("/menuopen/menuopenList");
    }

    @RequestMapping("/menuopenList2")
    @ResponseBody
    public Map getmenuopenList(int page, int rows) {
        //init
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> model = new HashMap<String, Object>();
        Pagination pagination = new Pagination(rows, page);
        map.put("pagination", pagination);

        List<Menuopen> menuopenEntity = new ArrayList<Menuopen>();
        try {
            menuopenEntity = menuopenService.getmenuopenList(map);
//            userAgents = userService.listPageUserAgentAndReferralUname(map);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        model.put("rows", menuopenEntity);
        model.put("total", pagination.getCount());
        return model;
    }

    @RequestMapping("/updateMenuopen")
    @ResponseBody
    public Map updateMenuopen(Menuopen menuopen) {
        Map<String, Object> model = new HashMap<>();
        int success = 0;
        String msg = "修改失败";

        try {
            success = menuopenService.updateByPrimaryKeySelective(menuopen);
            if (success == 1) {
                msg = "修改成功";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            success = 0;
            msg = "修改失败";
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }
}
