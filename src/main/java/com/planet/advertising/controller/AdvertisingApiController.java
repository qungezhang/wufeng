package com.planet.advertising.controller;

import com.planet.advertising.domain.Advertising;
import com.planet.advertising.service.AdvertisingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author winnie
 * @ClassName:AdvertisingApiController
 * @Description:广告接口类
 * @date 2018/10/22
 */
@Controller
@RequestMapping("/advertisingApi")
public class AdvertisingApiController {

    private static final Logger logger = LoggerFactory.getLogger(AdvertisingApiController.class);

    @Autowired
    private AdvertisingService advertisingService;

    /**
     * @api {get} /advertisingApi/list New获取广告列表
     * @apiDescription 根据类型获取广告列表
     * @apiName advertisingList
     * @apiGroup advertising
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} type 0-首页幻灯片，1-iOS的启动广告，2-安卓的启动广告，必填
     *
     * @apiSampleRequest /advertisingApi/list
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {JSONArray} result 响应内容,广告列表集合
     * @apiSuccess (success 200) {Number} result.id 广告主键
     * @apiSuccess (success 200) {String} result.imgname 广告名称
     * @apiSuccess (success 200) {String} result.imgurl 广告图片链接地址
     * @apiSuccess (success 200) {Number} result.isuse 是否使用，0-不使用，1-使用
     * @apiSuccess (success 200) {Number} result.sort 排序
     * @apiSuccess (success 200) {Date} result.createdate 创建时间
     * @apiSuccess (success 200) {Number} result.type 广告类型0-首页幻灯片，1-iOS的启动广告，2-安卓的启动广告
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *         [{
     *             "id": 2,
     *             "imgname": "第一套广告图",
     *             "imgurl": "7a51c73c-9007-4e2f-b0b1-18b3e4f201ef.jpg",
     *             "isuse": 1,
     *             "sort": 2,
     *             "createdate": 1497604987000,
     *             "type": 1
     *         }],
     *
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-获取广告列表信息失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取广告列表信息失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> advertisingList(int type) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        List<Advertising> advertisingList = new ArrayList<>();

        try {
            advertisingList = advertisingService.selectAdvertisingListByType(type);
            resp.put("result",advertisingList);
            code=200;
            msg = "ok";
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "获取广告列表失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }

}
