package com.planet.user.controller;

import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.common.util.DateTools;
import com.planet.job.domain.Job;
import com.planet.job.service.JobService;
import com.planet.ordpreorder.service.OrdPreOrderService;
import com.planet.point.domain.PointLog;
import com.planet.point.domain.Withdraw;
import com.planet.point.service.PointService;
import com.planet.point.service.WithdrawService;
import com.planet.user.domain.UserAgent;
import com.planet.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author winnie
 * @ClassName:UserApiController
 * @Description:用户个人中心积分接口类
 * @date 2018/8/13
 */
@Controller
@RequestMapping("/userPointApi")
public class UserPointApiController {


    private static final Logger logger = LoggerFactory.getLogger(UserPointApiController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private OrdPreOrderService ordPreOrderService;
    @Autowired
    private PointService pointService;
    @Autowired
    private JobService jobService;

    /**
     * @api {get} /userPointApi/get-userPoint New获取个人积分列表
     * @apiDescription 根据用户id获取该用户的积分明细
     * @apiName getUserPoint
     * @apiGroup user
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户主键ID，必填
     * @apiParam {Number} rows 当前页数据个数，必填
     * @apiParam {Number} page 当前页数，必填
     *
     * @apiSampleRequest /userPointApi/get-userPoint
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {JSONArray} result 响应内容,积分列表集合
     * @apiSuccess (success 200) {Number} result.id 积分日志主键
     * @apiSuccess (success 200) {Number} result.uid 用户id
     * @apiSuccess (success 200) {Double} result.afterpoint 修改后的积分数值
     * @apiSuccess (success 200) {Number} result.type 1-积分增加，2-积分减少
     * @apiSuccess (success 200) {String} result.msg 积分备注
     * @apiSuccess (success 200) {Date} result.createdate 创建时间
     * @apiSuccess (success 200) {String} result.tel 用户手机号
     * @apiSuccess (success 200) {Number} result.status 日志状态
     * @apiSuccess (success 200) {String} result.adminname 用户名称
     * @apiSuccess (success 200) {Number} result.pbid 能量球的主键id
     * @apiSuccess (success 200) {Number} result.getWay 获取方式：1-登录，2-注册，3-邀请，4-转发，5-认证，6-交易，7-提现
     *
     * @apiSuccess (success 200) {Object} userAgent 响应内容,个人信息
     * @apiSuccess (success 200) {Number} userAgent.iid 0
     * @apiSuccess (success 200) {Number} userAgent.isEngineer 积分ID
     * @apiSuccess (success 200) {Date} userAgent.lastlogindate 最后的登录时间
     * @apiSuccess (success 200) {Date} userAgent.logindate 登录日期
     * @apiSuccess (success 200) {String} userAgent.name 姓名
     * @apiSuccess (success 200) {String} userAgent.openid openid
     * @apiSuccess (success 200) {String} userAgent.password 密码
     * @apiSuccess (success 200) {Double} userAgent.point  积分
     * @apiSuccess (success 200) {Number} userAgent.referralUname referralUname
     * @apiSuccess (success 200) {Number} userAgent.referralcode referralcode
     * @apiSuccess (success 200) {Number} userAgent.referraluid referraluid
     * @apiSuccess (success 200) {Number} userAgent.remark 备注
     * @apiSuccess (success 200) {String} userAgent.tel 电话
     * @apiSuccess (success 200) {Number} userAgent.uid 用户id
     * @apiSuccess (success 200) {String} userAgent.username 用户名
     * @apiSuccess (success 200) {Number} userAgent.vip vip状态
     *
     * @apiSuccess (success 200) {Booelan} flag 用户是否可以提现：true-可以，false-不可以
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *         [{
     *              "id": 155,
     *             "afterpoint": 1044,
     *             "uid": 35,
     *             "type": 2,
     *             "point": 12,
     *             "msg": "积分提现",
     *             "createdate": 1534230124000,
     *             "tel": "13641704645",
     *             "status": 7,
     *             "adminname": "天天赚",
     *             "pbid": null,
     *             "getWay": 7
     *         }],
     *        "userAgent":
     *         {
     *           "iid":0,
     *           "isEngineer":1,
     *           "lastlogindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "logindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "name":"孙俊",
     *           "openid":"",
     *           "password":"yby567",
     *           "point":500,
     *          "qid":"",
     *          "referralUname":"",
     *          "referralcode":"J4XI",
     *           "referraluid":0,
     *           "remark":"南京港拓电气自动化-章海燕",
     *           "status":1,"tel":"13709503583",
     *           "uid":634,
     *           "username":"13709503583",
     *            "vip":1
     *         },
     *         "price":12,
     *         "code":200
     * }
     *
     * @apiError (error) {Number} code 400-获取积分列表信息失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取积分列表信息失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-userPoint")
    @ResponseBody
    public   Map<String, Object> getUserPoint(HttpServletRequest request, int rows, int page,Integer uid) {

        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        Pagination pagination = null;
        List<PointLog>  pointLogs= new ArrayList<>();
        try {
            Map<String, Object> map = new HashMap<>();
            pagination = new Pagination(rows, page);
            map.put("pagination", pagination);
            map.put("uid", uid);

            pointLogs = pointService.searchPointLogs(map);
            UserAgent userAgent = userService.selectByPrimaryKey(uid);

            Boolean flag = false;
            if(userAgent.getOrderAmount() != null){
                // 如果消费金额不为空，首先判断消费金额是否 >= 10000,如果 >= 10000 则更改状态为可提现true，并且标记状态为消费金额的提现状态
                if(userAgent.getOrderAmount().compareTo(new BigDecimal(10000)) >= 0){
                    flag = true;
                }else{
                    if(userAgent.getServiceNum() != null && userAgent.getServiceNum() >= 3){
                        flag = true;
                    }
                }
            }else{
                if(userAgent.getServiceNum() != null && userAgent.getServiceNum() >= 3){
                    flag = true;
                }
            }
            resp.put("flag",flag);
            resp.put("userAgent",userAgent);
            resp.put("result",pointLogs);
            code=200;
            msg = "ok";
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "获取积分列表信息失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }

    /**
     * @api {get} /userPointApi/get-pointRecord  New根据月份筛选提现记录
     * @apiDescription 根据用户id,月份根据月份筛选提现记录 获取该用户的提现记录
     * @apiName pointRecord
     * @apiGroup user
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户主键ID，必填
     * @apiParam {Number} rows 当前页数据总数，必填
     * @apiParam {Number} page 第几页，必填
     * @apiParam {String} month 搜索年-月份，必填
     *
     * @apiSampleRequest /userPointApi/get-pointRecord
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {JSONArray} result 响应内容,任务列表集合
     * @apiSuccess (success 200) {Number} result.wid 积分明细主键
     * @apiSuccess (success 200) {Number} result.uid 用户id
     * @apiSuccess (success 200) {String} result.username 用户名称
     * @apiSuccess (success 200) {String} result.mobile 用户手机号
     * @apiSuccess (success 200) {Number} result.type 提现类型：1支付宝；2微信
     * @apiSuccess (success 200) {String} result.account 提现账号
     * @apiSuccess (success 200) {Double} result.exchangePoint 兑换的积分
     * @apiSuccess (success 200) {Date} result.createTime 申请提现时间
     * @apiSuccess (success 200) {BigDecimal} result.amount 提现金额
     * @apiSuccess (success 200) {Integer} result.status 提现状态：0审核中；1已打款；2打款失败
     * @apiSuccess (success 200) {Date} result.transferTime 转账时间
     * @apiSuccess (success 200) {String} result.remark 备注
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *         [{
     *              "wid": "1",
     *             "uid": 35,
     *             "username": "123213",
     *             "mobile": "13641704645",
     *             "type": 1,
     *             "account": "1222",
     *             "exchangePoint": 0,
     *             "createTime": null,
     *             "amount": 1244,
     *             "status": 0,
     *             "transferTime": null,
     *             "remark": null
     *         }],
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-获取任务列表信息失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取积分列表信息失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-pointRecord")
    @ResponseBody
    public  Map<String, Object> pointRecord(HttpServletRequest request,int rows,int page,String month,Integer uid) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        Pagination pagination = null;
        List<Withdraw>  withdraws= new ArrayList<>();

        String date = month;


        try {
            Map<String, Object> map = new HashMap<>();
            pagination = new Pagination(rows, page);
            map.put("pagination", pagination);

            map.put("uid", uid);
            map.put("date",date);


            withdraws = withdrawService.listPageSelectWithdraw(map);

            resp.put("result",withdraws);
            code=200;
            msg = "ok";
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "获取积分列表信息失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }

    /**
     * @api {get} /userPointApi/apply-point New申请提现
     * @apiDescription 根据用户id,申请提现积分
     * @apiName applyPoint
     * @apiGroup user
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户主键ID，必填
     * @apiParam {Number} point 用户提现的积分，必填
     * @apiParam {String} mobile 用户提现的积分账户，必填
     * @apiParam {Number} type 用户提现的类型 1支付宝；2微信，必填
     *
     * @apiSampleRequest /userPointApi/apply-point
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容,任务列表集合
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":null,
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-申请积分提现失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"申请积分提现失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/apply-point")
    @ResponseBody
    public  Map<String, Object> applyPoint(HttpServletRequest request,Integer uid,Double point,String mobile,Integer type) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        try {
            UserAgent userAgent = userService.selectByPrimaryKey(uid);
            Withdraw withdraw = new Withdraw();
            withdraw.setUid(uid);
            withdraw.setType(type);
            withdraw.setUsername(userAgent.getUsername());
            withdraw.setAccount(mobile);
            withdraw.setExchangePoint(point);
            withdraw.setAmount(new BigDecimal(point));
            withdraw.setStatus(0);
            withdraw.setCreateTime(new Date());
            Integer withdrawConsumeType = 0;
            BigDecimal withdrawBeforeAmount = new BigDecimal(0);
            if(userAgent.getOrderAmount() != null){
                // 如果消费金额不为空，首先判断消费金额是否 >= 10000,如果 >= 10000 则更改状态为可提现true，并且标记状态为消费金额的提现状态
                if(userAgent.getOrderAmount().compareTo(new BigDecimal(10000)) >= 0){
                    withdrawBeforeAmount = userAgent.getOrderAmount();
                    // 提交提现申请后，清除用户的消费总额
                    userAgent.setOrderAmount(new BigDecimal(0));
                    userService.updateByPrimaryKey(userAgent);
                }else{
                    if(userAgent.getServiceNum() != null && userAgent.getServiceNum() >= 3){
                        withdrawConsumeType = 1;
                        // 提交提现申请后，扣除用户服务的次数。3次为一次提现机会所以扣除3次
                        userAgent.setServiceNum(userAgent.getServiceNum() - 3);
                        userService.updateByPrimaryKey(userAgent);
                    }
                }
            }else{
                if(userAgent.getServiceNum() != null && userAgent.getServiceNum() >= 3){
                    withdrawConsumeType = 1;
                    // 提交提现申请后，扣除用户服务的次数。3次为一次提现机会所以扣除3次
                    userAgent.setServiceNum(userAgent.getServiceNum() - 3);
                    userService.updateByPrimaryKey(userAgent);
                }
            }

            withdraw.setWithdrawConsumeType(withdrawConsumeType);
            withdraw.setWithdrawBeforeAmount(withdrawBeforeAmount);
            int i = withdrawService.insertByPrimaryKeySelective(withdraw);
            if(i > 0){
                resp.put("result",null);
                code=200;
                msg = "ok";
            }else{
                resp.put("result",null);
                code=500;
                msg = "申请积分提现失败";
            }
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "申请积分提现失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }

    /**
     * @api {get} /userPointApi/get-userPoint2 New获取个人积分列表2(h5端)
     * @apiDescription 根据用户id获取该用户的积分明细，返回接口数据不正规，重新修改过后的接口(h5端)
     * @apiName getUserPoint2
     * @apiGroup user
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户主键ID，必填
     * @apiParam {Number} rows 当前页数据个数，必填
     * @apiParam {Number} page 当前页数，必填
     *
     * @apiSampleRequest /userPointApi/get-userPoint2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {JSON} result 响应内容
     * @apiSuccess (success 200) {JSONArray} result.pointLog 响应内容,积分列表集合
     * @apiSuccess (success 200) {Number} result.pointLog.id 积分日志主键
     * @apiSuccess (success 200) {Number} result.pointLog.uid 用户id
     * @apiSuccess (success 200) {Double} result.pointLog.afterpoint 修改后的积分数值
     * @apiSuccess (success 200) {Number} result.pointLog.type 1-积分增加，2-积分减少
     * @apiSuccess (success 200) {String} result.pointLog.msg 积分备注
     * @apiSuccess (success 200) {Date} result.pointLog.createdate 创建时间
     * @apiSuccess (success 200) {String} result.pointLog.tel 用户手机号
     * @apiSuccess (success 200) {Number} result.pointLog.status 日志状态
     * @apiSuccess (success 200) {String} result.pointLog.adminname 用户名称
     * @apiSuccess (success 200) {Number} result.pointLog.pbid 能量球的主键id
     * @apiSuccess (success 200) {Number} result.pointLog.getWay 获取方式：1-登录，2-注册，3-邀请，4-转发，5-认证，6-交易，7-提现
     *
     * @apiSuccess (success 200) {Object} result.userAgent 响应内容,个人信息
     * @apiSuccess (success 200) {Number} result.userAgent.iid 0
     * @apiSuccess (success 200) {Number} result.userAgent.isEngineer 积分ID
     * @apiSuccess (success 200) {Date} result.userAgent.lastlogindate 最后的登录时间
     * @apiSuccess (success 200) {Date} result.userAgent.logindate 登录日期
     * @apiSuccess (success 200) {String} result.userAgent.name 姓名
     * @apiSuccess (success 200) {String} result.userAgent.openid openid
     * @apiSuccess (success 200) {String} result.userAgent.password 密码
     * @apiSuccess (success 200) {Double} result.userAgent.point  积分
     * @apiSuccess (success 200) {Number} result.userAgent.referralUname referralUname
     * @apiSuccess (success 200) {Number} result.userAgent.referralcode referralcode
     * @apiSuccess (success 200) {Number} result.userAgent.referraluid referraluid
     * @apiSuccess (success 200) {Number} result.userAgent.remark 备注
     * @apiSuccess (success 200) {String} result.userAgent.tel 电话
     * @apiSuccess (success 200) {Number} result.userAgent.uid 用户id
     * @apiSuccess (success 200) {String} result.userAgent.username 用户名
     * @apiSuccess (success 200) {Number} result.userAgent.vip vip状态
     *
     * @apiSuccess (success 200) {Number} result.flag 用户是否可提现 true-可以，false-不可以
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":{
     *         "pointLog":[{
     *              "id": 155,
     *             "afterpoint": 1044,
     *             "uid": 35,
     *             "type": 2,
     *             "point": 12,
     *             "msg": "积分提现",
     *             "createdate": 1534230124000,
     *             "tel": "13641704645",
     *             "status": 7,
     *             "adminname": "天天赚",
     *             "pbid": null,
     *             "getWay": 7
     *         }],
     *        "userAgent":
     *         {
     *           "iid":0,
     *           "isEngineer":1,
     *           "lastlogindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "logindate":{"date":27,"day":4,"hours":11,"minutes":29,"month":6,"seconds":1,"time":1501126141000,"timezoneOffset":-480,"year":117},
     *           "name":"孙俊",
     *           "openid":"",
     *           "password":"yby567",
     *           "point":500,
     *          "qid":"",
     *          "referralUname":"",
     *          "referralcode":"J4XI",
     *           "referraluid":0,
     *           "remark":"南京港拓电气自动化-章海燕",
     *           "status":1,"tel":"13709503583",
     *           "uid":634,
     *           "username":"13709503583",
     *            "vip":1
     *         },
     *         "price":12
     *     },
     *         "code":200
     * }
     *
     * @apiError (error) {Number} code 400-获取积分列表信息失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取积分列表信息失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-userPoint2")
    @ResponseBody
    public   Map<String, Object> getUserPoint2(HttpServletRequest request, int rows, int page,Integer uid) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        Pagination pagination = null;
        List<PointLog>  pointLogs= new ArrayList<>();
        Map<String,Object> result = new HashMap<>();
        try {
            Map<String, Object> map = new HashMap<>();
            pagination = new Pagination(rows, page);
            map.put("pagination", pagination);

            map.put("uid", uid);

            pointLogs = pointService.searchPointLogs(map);
            UserAgent userAgent = userService.selectByPrimaryKey(uid);

            Boolean flag = true;
            if(userAgent.getOrderAmount() != null){
                if(new BigDecimal(10000).compareTo(userAgent.getOrderAmount()) > 0){
                    flag = false;
                    if(userAgent.getServiceNum() != null && userAgent.getServiceNum() > 3){
                        userAgent.setServiceNum(0);
                        userService.updateByPrimaryKey(userAgent);
                        flag = true;
                    }
                }else{
                    flag = true;
                    userAgent.setOrderAmount(new BigDecimal(0.00));
                    userService.updateByPrimaryKey(userAgent);
                }
            }else{
                flag = false;
                if(userAgent.getServiceNum() != null && userAgent.getServiceNum() >= 3){
                    userAgent.setServiceNum(userAgent.getServiceNum() - 3);
                    userService.updateByPrimaryKey(userAgent);
                    flag = true;
                }
            }

            resp.put("flag",flag);
            result.put("userAgent",userAgent);
            result.put("pointLog",pointLogs);
            resp.put("result",result);
            code=200;
            msg = "ok";
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "获取积分列表信息失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }




}
