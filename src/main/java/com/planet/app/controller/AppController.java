package com.planet.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * app的控制器
 * Created by Qinghua on 2016-01-31 10:40.
 */
@Controller
@RequestMapping("/app")
public class AppController {
    @RequestMapping("/index")
    public ModelAndView appView() {
        Map<String, Object> data = new HashMap<>();
        return new ModelAndView("/wechat/index",data);
    }

    /**
     * @api {get} /app/about_us.html 关于我们页面
     * @apiDescription 关于我们页面
     * @apiName aboutUs
     * @apiGroup app
     * @apiVersion 2.0.0
     */
    @RequestMapping("/aboutUs")
    public String aboutUs(){
        return "redirect:/app/about_us.html";
    }

    /**
     * @api {get} /app/bank_info.html 公司账号页面
     * @apiDescription 公司账号页面
     * @apiName bankInfo
     * @apiGroup app
     * @apiVersion 2.0.0
     */
    @RequestMapping("/bankInfo")
    public String bankInfo(){
        return "redirect:/app/bank_info.html";
    }
    
    /**
     * @api {get} /app/balloon.html 能量气球页
     * @apiDescription 能量气球页
     * @apiParam {Number} uid *用户ID
     * @apiName balloon
     * @apiGroup app
     * @apiVersion 2.0.0
     */
    @RequestMapping("/balloon")
    public String balloon(){
        return "redirect:/app/balloon.html";
    }
}
