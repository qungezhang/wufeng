package com.planet.config.controller;

import com.planet.config.domain.Config;
import com.planet.config.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Qinghua on 2016-03-21 11:19.
 */
@Controller
@RequestMapping("/config")
public class ConfigController {
    private static final Logger logger =
            LoggerFactory.getLogger(ConfigController.class);
    @Autowired
    ConfigService configService;

    /**
     * @api {get} /config/get-config 获取配置信息
     * @apiDescription 根据配置ID获取配置信息
     * @apiName getSysConfig
     * @apiGroup config
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} configId 配置ID，非必填，无效参数只查ID为1的数据
     *
     * @apiSampleRequest /config/get-config
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容，一般为空
     * @apiSuccess (success 200) {Number} result.id 配置ID
     * @apiSuccess (success 200) {String} result.aboutus 关于我们
     * @apiSuccess (success 200) {String} result.term 条款
     * @apiSuccess (success 200) {String} result.bank 公司
     * @apiSuccess (success 200) {String} result.account 账号
     * @apiSuccess (success 200) {String} result.tax 税号
     * @apiSuccess (success 200) {Number} result.filesize 文件限制大小
     * @apiSuccess (success 200) {String} result.tel 电话
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *          {
     *              "id":1,
     *              "aboutus":null,
     *              "term":null,
     *              "bank":null,
     *              "account":null,
     *              "tax":null,
     *              "filesize":null,
     *              "tel":"021-51082175"
     *          },
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 60012-获取配置信息失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取配置信息失败",
     *      "result":null,
     *      "code":60012
     * }
     */
    @ResponseBody
    @RequestMapping("/get-config")
    public Map<String,Object> getSysConfig(Integer configId){
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        try {
            Config config = configService.selectByPrimaryKey(configId);
            resp.put("result",config);
            code = 200;
            msg = "ok";
        } catch (Exception e) {
            logger.error("GetSysConfig Error:",e);
            code = 60012;
            msg = "获取配置信息失败";
        }
        resp.put("code", code);
        resp.put("message", msg);
        return resp;
    };
}
