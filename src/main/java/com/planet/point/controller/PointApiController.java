package com.planet.point.controller;

import com.planet.point.domain.PointBalloon;
import com.planet.point.service.PointBalloonService;
import com.planet.point.service.PointService;
import com.planet.user.domain.UserAgent;
import com.planet.user.service.UserService;
import com.planet.utils.TokenProccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.List;

/**
 * Created by winnie on 2018/8/6.
 */
@Controller
@RequestMapping("/pointApi")
public class PointApiController {

    private static final Logger logger = LoggerFactory.getLogger(PointApiController.class);

    @Autowired
    private PointBalloonService pointBalloonService;
    @Autowired
    private UserService userService;
    @Autowired
    private PointService pointService;


    /**
     * @api {get} /pointApi/get-pointRanking New获取积分信息
     * @apiDescription 根据uid获取当前用户的积分
     * @apiName getPointRanking
     * @apiGroup point
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户主键ID，必填
     *
     * @apiSampleRequest /pointApi/get-pointRanking
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} userAgent 用户
     * @apiSuccess (success 200) {Number} userAgent.iid 发票id
     * @apiSuccess (success 200) {Number} userAgent.isEngineer 积分ID
     * @apiSuccess (success 200) {Date} userAgent.lastlogindate 最后的登录时间
     * @apiSuccess (success 200) {Date} userAgent.logindate 登录日期
     * @apiSuccess (success 200) {String} userAgent.name 姓名
     * @apiSuccess (success 200) {String} userAgent.openid openid
     * @apiSuccess (success 200) {String} userAgent.password 密码
     * @apiSuccess (success 200) {Number} userAgent.point  积分
     * @apiSuccess (success 200) {String} userAgent.referralUname referralUname
     * @apiSuccess (success 200) {String} userAgent.referralcode referralcode
     * @apiSuccess (success 200) {String} userAgent.referraluid referraluid
     * @apiSuccess (success 200) {String} userAgent.remark 备注
     * @apiSuccess (success 200) {String} userAgent.tel 电话
     * @apiSuccess (success 200) {Number} userAgent.uid 用户id
     * @apiSuccess (success 200) {String} userAgent.username 用户名
     * @apiSuccess (success 200) {Number} userAgent.vip vip状态
     * @apiSuccess (success 200) {JSONArray}userAgentList 多个用户排名的集合
     * @apiSuccess (success 200) {Number} userAgentList.iid 发票id
     * @apiSuccess (success 200) {Number} userAgentList.isEngineer 积分ID
     * @apiSuccess (success 200) {Date} userAgentList.lastlogindate 最后的登录时间
     * @apiSuccess (success 200) {Date} userAgentList.logindate 登录日期
     * @apiSuccess (success 200) {String} userAgentList.name 姓名
     * @apiSuccess (success 200) {String} userAgentList.openid openid
     * @apiSuccess (success 200) {String} userAgentList.password 密码
     * @apiSuccess (success 200) {Number} userAgentList.point  积分
     * @apiSuccess (success 200) {String} userAgentList.referralUname referralUname
     * @apiSuccess (success 200) {String} userAgentList.referralcode referralcode
     * @apiSuccess (success 200) {String} userAgentList.referraluid referraluid
     * @apiSuccess (success 200) {String} userAgentList.remark 备注
     * @apiSuccess (success 200) {String} userAgentList.tel 电话
     * @apiSuccess (success 200) {Number} userAgentList.uid 用户id
     * @apiSuccess (success 200) {String} userAgentList.username 用户名
     * @apiSuccess (success 200) {Number} userAgentList.vip vip状态
     * @apiSuccess (success 200) {JSONArray}pointBalloonList 能量起泡球集合
     * @apiSuccess (success 200) {Number}pointBalloonList.id id
     * @apiSuccess (success 200) {Number}pointBalloonList.type 产生的类型：1-自动产生的气泡；2-用户行为产生的气泡
     * @apiSuccess (success 200) {Date}pointBalloonList.createTime 产生时间
     * @apiSuccess (success 200) {Date}pointBalloonList.endTime 领取截止的时间
     * @apiSuccess (success 200) {String} pointBalloonList.name 能量名称
     * @apiSuccess (success 200) {String} pointBalloonList.icon 图标
     * @apiSuccess (success 200) {Number} pointBalloonList.oid 订单id
     * @apiSuccess (success 200) {Number} result.pointBalloonList.getWay 产生能量气泡的方式： 1 注册 2 认证（申请VIP） 3 交易 > 邀请码注册 5 转发 6 默认
     * @apiSuccess (success 200) {Number} result.pointBalloonList.point 积分
     * @apiSuccess (success 200) {String} result.pointBalloonList.remark 备注
     * @apiSuccess (success 200) {Number} result.pointBalloonList.uid 用户id
     * @apiSuccess (success 200) {Number} result.pointBalloonList.status 用户状态起泡球摘取状态：0 未摘取；1 已摘取
     * @apiSuccess (success 200) {Number} result.pointBalloonList.energyStatus 气泡状态：0 未产生；1 已产生
     * @apiSuccess (success 200) {Number} result.pointBalloonList.brandEnergyStatus 系统自动生成泡球状态：0 未产生；1 已产生
     * @apiSuccess (success 200) {Number} result.pointBalloonList.sort 优先级排序：1 注册 > 2 认证（申请VIP） > 3 交易 > 4 邀请码注册 > 5 转发 > 6 默认 > 8 已生成未摘取 > 9 未生成未摘取 > 10 已生成已摘取 > 11 其他
     * @apiSuccess (success 200) {Number} pointBalloonList.time 生成能量球的时间
     *
     * @apiSuccess (success 200) {Number} rank 当前用户的排名
     *
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "userAgent":
     *         {
     *           "iid":0,
     *           "isEngineer":1,
     *           "lastlogindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "logindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "name":"孙俊",
     *           "openid":"",
     *           "password":"yby567",
     *           "point":500,
     *           "qid":"",
     *           "referralUname":"",
     *           "referralcode":"J4XI",
     *           "referraluid":0,
     *           "remark":"南京港拓电气自动化-章海燕",
     *           "status":1,"tel":"13709503583",
     *           "uid":634,
     *           "username":"13709503583",
     *           "vip":1
     *         },
     *       "pointBalloonList":[{
     *             "id": 16,
     *             "type": 1,
     *             "createTime": null,
     *             "endTime": null,
     *             "name": "台达",
     *             "icon": "79f1a90d-7600-4893-98a4-be02d5c6ef54taida.jpeg",
     *             "oid": null,
     *             "getWay": 2,
     *             "point": 0.25,
     *             "remark": null,
     *             "uid": null,
     *             "status": 0,
     *             "energyStatus": 1,
     *             "brandEnergyStatus":1,
     *             "sort":9;
     *             "time":"04:40:00"
     *         }],
     *          "rank": 12，
     *         "userAgentList":
     *          [{
     *            "iid":0,
     *           "isEngineer":1,
     *           "lastlogindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "logindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "name":"孙俊",
     *           "openid":"",
     *           "password":"yby567",
     *           "point":500,
     *           "qid":"",
     *           "referralUname":"",
     *           "referralcode":"J4XI",
     *           "referraluid":0,
     *           "remark":"南京港拓电气自动化-章海燕",
     *           "status":1,"tel":"13709503583",
     *           "uid":634,
     *           "username":"13709503583",
     *           "vip":1
     *          }],
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-获取积分信息失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取积分信息失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-pointRanking")
    @ResponseBody
    public Map<String, Object> getPointRanking(Integer uid, HttpServletRequest request) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        try {
            List<UserAgent> userAgentList = userService.getPointRanking();
            UserAgent userAgent = userService.selectByPrimaryKey(uid);
            if(null != userAgent){
                if(userAgentList != null && userAgentList.size()>=10){
                    if(userAgent.getPoint() <= userAgentList.get(9).getPoint()){
                        userAgentList.add(userAgent);
                    }
                }
            }
            // 隐藏用户名中间字符串
            this.hideMobileMiddle(userAgentList,uid);

            List<PointBalloon> pointBalloonList = this.list(uid);
            // 对能量球进行排序
//            this.sort(pointBalloonList);

            Integer rank = userService.findUserRank(uid);

            code=200;
            msg = "ok";
            resp.put("userAgent",userAgent);
            resp.put("userAgentList",userAgentList);
            resp.put("rank",rank);
            resp.put("pointBalloonList",pointBalloonList);
        } catch (Exception e) {
            logger.error("GetPoint Error:",e);
            code = 500;
            msg = "获取积分信息失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;

    }

    /**
     * @api {get} /pointApi/gather-energyBall New收取能量球
     * @apiDescription 根据uid,pointBalloonId 给当前用户增加积分
     * @apiName gatherEnergyBall
     * @apiGroup point
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户ID，必填
     * @apiParam {Number} pointBalloonId 能量球ID，必填
     *
     * @apiSampleRequest /pointApi/get-energyBall
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容，一般为空
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":null,
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-收取积分失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"收取积分失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/gather-energyBall")
    @ResponseBody
    public Map<String, Object> gatherEnergyBall(HttpServletResponse response,Integer uid,Integer pointBalloonId, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("P3P", "CP=CAO PSA OUR");
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            response.addHeader("Access-Control-Allow-Methods", "POST,GET,TRACE,OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,Origin,Accept");
            response.addHeader("Access-Control-Max-Age", "120");
        }
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        try {
            int num = pointService.gatherEnergyBall(uid, pointBalloonId,null);
            List<PointBalloon> pointBalloonList = this.list(uid);

            code=200;
            msg = "ok";
            resp.put("result",1);
            resp.put("pointBalloonList",pointBalloonList);
        } catch (Exception e) {
            logger.error("GetPoint Error:",e);
            code = 500;
            msg = "收取积分失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;

    }

    /**
     * @api {get} /pointApi/get-pointRanking2 New获取积分信息2(h5端)
     * @apiDescription 根据uid获取当前用户的积分,返回接口数据不正规，重新修改过后的接口
     * @apiName getPointRanking2
     * @apiGroup point
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户主键ID，必填
     *
     * @apiSampleRequest /pointApi/get-pointRanking2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {JSON} result 响应内容
     * @apiSuccess (success 200) {Object} result.userAgent 用户
     * @apiSuccess (success 200) {Number} result.userAgent.iid 发票id
     * @apiSuccess (success 200) {Number} result.userAgent.isEngineer 积分ID
     * @apiSuccess (success 200) {Date}   result.userAgent.lastlogindate 最后的登录时间
     * @apiSuccess (success 200) {Date}   result.userAgent.logindate 登录日期
     * @apiSuccess (success 200) {String} result.userAgent.name 姓名
     * @apiSuccess (success 200) {String} result.userAgent.openid openid
     * @apiSuccess (success 200) {String} result.userAgent.password 密码
     * @apiSuccess (success 200) {Number} result.userAgent.point  积分
     * @apiSuccess (success 200) {String} result.userAgent.referralUname referralUname
     * @apiSuccess (success 200) {String} result.userAgent.referralcode referralcode
     * @apiSuccess (success 200) {String} result.userAgent.referraluid referraluid
     * @apiSuccess (success 200) {String} result.userAgent.remark 备注
     * @apiSuccess (success 200) {String} result.userAgent.tel 电话
     * @apiSuccess (success 200) {Number} result.userAgent.uid 用户id
     * @apiSuccess (success 200) {String} result.userAgent.username 用户名
     * @apiSuccess (success 200) {Number} result.userAgent.vip vip状态
     * @apiSuccess (success 200) {JSONArray}result.userAgentList 多个用户排名的集合
     * @apiSuccess (success 200) {Number} result.userAgentList.iid 发票id
     * @apiSuccess (success 200) {Number} result.userAgentList.isEngineer 积分ID
     * @apiSuccess (success 200) {Date} uresult.serAgentList.lastlogindate 最后的登录时间
     * @apiSuccess (success 200) {Date} result.userAgentList.logindate 登录日期
     * @apiSuccess (success 200) {String} result.userAgentList.name 姓名
     * @apiSuccess (success 200) {String} result.userAgentList.openid openid
     * @apiSuccess (success 200) {String} result.userAgentList.password 密码
     * @apiSuccess (success 200) {Number} result.userAgentList.point  积分
     * @apiSuccess (success 200) {String} result.userAgentList.referralUname referralUname
     * @apiSuccess (success 200) {String} result.userAgentList.referralcode referralcode
     * @apiSuccess (success 200) {String} result.userAgentList.referraluid referraluid
     * @apiSuccess (success 200) {String} result.userAgentList.remark 备注
     * @apiSuccess (success 200) {String} result.userAgentList.tel 电话
     * @apiSuccess (success 200) {Number} result.userAgentList.uid 用户id
     * @apiSuccess (success 200) {String} result.userAgentList.username 用户名
     * @apiSuccess (success 200) {Number} result.userAgentList.vip vip状态
     * @apiSuccess (success 200) {JSONArray}result.pointBalloonList 能量起泡球集合
     * @apiSuccess (success 200) {Number}result.pointBalloonList.id id
     * @apiSuccess (success 200) {Number}result.pointBalloonList.type 产生的类型：1-自动产生的气泡；2-用户行为产生的气泡
     * @apiSuccess (success 200) {Date}result.pointBalloonList.createTime 产生时间
     * @apiSuccess (success 200) {Date}result.pointBalloonList.endTime 领取截止的时间
     * @apiSuccess (success 200) {String} result.pointBalloonList.name 能量名称
     * @apiSuccess (success 200) {String} result.pointBalloonList.icon 图标
     * @apiSuccess (success 200) {Number} result.pointBalloonList.oid 订单id
     * @apiSuccess (success 200) {Number} result.pointBalloonList.getWay 产生能量气泡的方式： 1 注册 2 认证（申请VIP） 3 交易 > 邀请码注册 5 转发 6 默认
     * @apiSuccess (success 200) {Number} result.pointBalloonList.point 积分
     * @apiSuccess (success 200) {String} result.pointBalloonList.remark 备注
     * @apiSuccess (success 200) {Number} result.pointBalloonList.uid 用户id
     * @apiSuccess (success 200) {Number} result.pointBalloonList.status 用户状态起泡球摘取状态：0 未摘取；1 已摘取
     * @apiSuccess (success 200) {Number} result.pointBalloonList.energyStatus 气泡状态：0 未产生；1 已产生
     * @apiSuccess (success 200) {Number} result.pointBalloonList.brandEnergyStatus 系统自动生成泡球状态：0 未产生；1 已产生
     * @apiSuccess (success 200) {Number} result.pointBalloonList.sort 优先级排序：1 注册 > 2 认证（申请VIP） > 3 交易 > 4 邀请码注册 > 5 转发 > 6 默认 > 8 已生成未摘取 > 9 未生成未摘取 > 10 已生成已摘取 > 11 其他
     * @apiSuccess (success 200) {Number} pointBalloonList.time 生成能量球的时间
     *
     * @apiSuccess (success 200) {Number} result.rank 当前用户的排名
     *
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":{
     *          "userAgent":
     *         {
     *           "iid":0,
     *           "isEngineer":1,
     *           "lastlogindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "logindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "name":"孙俊",
     *           "openid":"",
     *           "password":"yby567",
     *           "point":500,
     *           "qid":"",
     *           "referralUname":"",
     *           "referralcode":"J4XI",
     *           "referraluid":0,
     *           "remark":"南京港拓电气自动化-章海燕",
     *           "status":1,"tel":"13709503583",
     *           "uid":634,
     *           "username":"13709503583",
     *           "vip":1
     *         },
     *       "pointBalloonList":[{
     *             "id": 16,
     *             "type": 1,
     *             "createTime": null,
     *             "endTime": null,
     *             "name": "台达",
     *             "icon": "79f1a90d-7600-4893-98a4-be02d5c6ef54taida.jpeg",
     *             "oid": null,
     *             "getWay": 2,
     *             "point": 0.25,
     *             "remark": null,
     *             "uid": null,
     *             "status": 0,
     *             "energyStatus": 1,
     *             "brandEnergyStatus":1,
     *             "sort":9;
     *             "time":"04:40:00"
     *         }],
     *         "userAgentList":
     *          [{
     *            "iid":0,
     *           "isEngineer":1,
     *           "lastlogindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "logindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "name":"孙俊",
     *           "openid":"",
     *           "password":"yby567",
     *           "point":500,
     *           "qid":"",
     *           "referralUname":"",
     *           "referralcode":"J4XI",
     *           "referraluid":0,
     *           "remark":"南京港拓电气自动化-章海燕",
     *           "status":1,"tel":"13709503583",
     *           "uid":634,
     *           "username":"13709503583",
     *           "vip":1
     *          }],
     *          "rank": 12
     *      }
     *
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-获取积分信息失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取积分信息失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-pointRanking2")
    @ResponseBody
    public Map<String,Object> getPointRanking2(Integer uid, HttpServletRequest request,HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("P3P", "CP=CAO PSA OUR");
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            response.addHeader("Access-Control-Allow-Methods", "POST,GET,TRACE,OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,Origin,Accept");
            response.addHeader("Access-Control-Max-Age", "120");
        }

        List<Map<String,Object>> resultList = new ArrayList<>();
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        Map<String,Object> result = new HashMap<>();
        try {
            List<UserAgent> userAgentList = userService.getPointRanking();
            UserAgent userAgent = userService.selectByPrimaryKey(uid);
            if(null == userAgent){
                resp.put("code", "500");
                resp.put("message", "用户不存在！");

                resultList.add(resp);
                return resp;
            }
            if(userAgentList != null && userAgentList.size()>=10){
                if(userAgent.getPoint() <= userAgentList.get(9).getPoint()){
                    userAgentList.add(userAgent);
                }
            }
            // 隐藏用户名中间字符串
            this.hideMobileMiddle(userAgentList,uid);
            Integer rank = userService.findUserRank(uid);

            result.put("userAgentList",userAgentList);
            result.put("rank",rank);

            List<PointBalloon> pointBalloonList = this.list(uid);
            // 对能量球进行排序
//            this.sort(pointBalloonList);
            result.put("pointBalloonList",pointBalloonList);

            result.put("userAgent",userAgent);
            TokenProccessor tokenProccessor = TokenProccessor.getInstance();
            result.put("token",tokenProccessor.makeToken());
            resp.put("result",result);
            code=200;
            msg = "ok";
        } catch (Exception e) {
            logger.error("GetPoint Error:",e);
            code = 500;
            msg = "获取积分信息失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        resultList.add(resp);
        return resp;

    }

    /**
     * @api {get} /pointApi/gather-energyBall2 New收取能量球2(h5端)
     * @apiDescription 根据uid,pointBalloonId 给当前用户增加积分，修改返回数据(h5端)
     * @apiName gatherEnergyBall2
     * @apiGroup point
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户ID，必填
     * @apiParam {Number} pointBalloonId 能量球ID，必填
     *
     * @apiSampleRequest /pointApi/get-energyBall2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容，一般为空
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":"1",
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-收取积分失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"收取积分失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/gather-energyBall2")
    @ResponseBody
    public Map<String, Object> gatherEnergyBall2(HttpServletResponse response,Integer uid, Integer pointBalloonId, String token, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("P3P", "CP=CAO PSA OUR");
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            response.addHeader("Access-Control-Allow-Methods", "POST,GET,TRACE,OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,Origin,Accept");
            response.addHeader("Access-Control-Max-Age", "120");
        }
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;

        try {
            boolean b = isRepeatSubmit(request,token);//判断用户是否是重复提交
            if( b == true ) {
                code = 500;
                msg = "数据已提交";
                resp.put("code", code);
                resp.put("message", msg);

                return resp;
            }
            request.getSession().removeAttribute("token");//移除session中的token
            Thread.sleep(900);
            pointService.gatherEnergyBall(uid, pointBalloonId,null);
            List<PointBalloon> pointBalloonList = this.list(uid);
            code=200;
            msg = "ok";
            resp.put("result",1);
            resp.put("pointBalloonList",pointBalloonList);
            TokenProccessor tokenProccessor = TokenProccessor.getInstance();
            resp.put("token",tokenProccessor.makeToken());
        } catch (Exception e) {
            logger.error("GetPoint Error:",e);
            code = 500;
            msg = "收取积分失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;

    }

    /**
     * @version 1.0.0
     * @description 隐藏中间手机四位
     * @author aiveily
     * @date 2018/9/18 18:48
     */
    private void hideMobileMiddle(List<UserAgent> userAgentList,Integer uid){
        if(userAgentList.size()>0){
            for(UserAgent user : userAgentList){
                if (user.getUid().equals(uid)) {
                    continue;
                }
                String username = user.getUsername();
                String temp = "*";
                if(!"".equals(username) && username != null && username.length() >= 8){
                    String tempStr = username.substring(4,username.length()-4);
                    for (int i = 0; i < tempStr.length() ; i++) {
                        temp += "*";
                    }
                    String phoneNumber = username.substring(0, 3) + temp + username.substring(username.length()-4, username.length());
                    user.setUsername(phoneNumber);
                } else {
                    String tempStr = username.substring(2,username.length()-2);
                    for (int i = 0; i < tempStr.length()-1; i++) {
                        temp += "*";
                    }
                    String phoneNumber = username.substring(0, 2) + temp + username.substring(username.length()-2, username.length());
                    user.setUsername(phoneNumber);
                }
            }
        }
    }

    /**
     * 根据id查询能量球列表
     * @param uid
     * @return
     */
    private List<PointBalloon> list(Integer uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        List<PointBalloon> pointBalloonList = pointBalloonService.getPointBalloonList(map);
        return pointBalloonList;
    }

    public boolean isRepeatSubmit(HttpServletRequest request,String token){
        String client_token = token;
        // 1、如果用户提交的表单数据中没有token，则用户是重复提交了表单
        if(client_token==null) {
            return true;
        }
        // 取出存储在Session中的token
        String server_token = (String) request.getSession().getAttribute("token");
        // 2、如果当前用户的Session中不存在Token(令牌)，则用户是重复提交了表单
        if(server_token==null){
            return true;
        }
        // 3、存储在Session中的Token(令牌)与表单提交的Token(令牌)不同，则用户是重复提交了表单
        if(!client_token.equals(server_token)){
            return true;
        }
        return false;
    }

}
