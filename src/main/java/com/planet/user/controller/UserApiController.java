package com.planet.user.controller;

import com.planet.common.Constant;
import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.job.domain.Job;
import com.planet.job.domain.UserJob;
import com.planet.job.service.JobService;
import com.planet.job.service.UserJobService;
import com.planet.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @author winnie
 * @ClassName:UserApiController
 * @Description:用户个人中心任务管理接口类
 * @date 2018/8/10
 */
@Controller
@RequestMapping("/userApi")
public class UserApiController {


    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private JobService jobService;
    @Autowired
    private UserJobService userJobService;
    @Autowired
    private UserService userService;

    /**
     * @api {get} /userApi/get-userJob New获取个人任务列表
     * @apiDescription 根据用户id获取该用户的任务列表
     * @apiName getUserJobByUserId
     * @apiGroup user
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户主键ID，必填
     * @apiParam {Number} rows 当前页数据个数，必填
     * @apiParam {Number} page 当前页数，必填
     *
     * @apiSampleRequest /userApi/get-userJob
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {JSONArray} result 响应内容,任务列表集合
     * @apiSuccess (success 200) {String} result.jid 任务主键
     * @apiSuccess (success 200) {String} result.name 任务名称
     * @apiSuccess (success 200) {String} result.subhead 副标题
     * @apiSuccess (success 200) {Date} result.startTime 开始时间
     * @apiSuccess (success 200) {Date} result.endTime 结束时间
     * @apiSuccess (success 200) {String} result.address 地址
     * @apiSuccess (success 200) {String} result.tags 标签
     * @apiSuccess (success 200) {String} result.description 内容
     * @apiSuccess (success 200) {BigDecimal} result.price 价格
     * @apiSuccess (success 200) {Number} result.status  状态：0 待领取；1 待审核；2 审核通过；4 任务关闭；5 任务完成
     *                                                   关于我的任务的几个状态：默认（待审核）
     *                                                                      if status = 2 （审核通过）
     *                                                                      else if status = 4 （任务已下架）
     *                                                                      else if status = 5 （任务已完成）
     *                                                                      else
     *                                                                          if userJobStatus = 1 (放弃任务)
     *                                                                          if examineStatus = 1 （审核不通过）
     *
     * @apiSuccess (success 200) {Date} result.createTime 创建时间
     * @apiSuccess (success 200) {Date} result.modifyTime 修改时间
     * @apiSuccess (success 200) {Number} result.isDelete 删除判断
     * @apiSuccess (success 200) {Number} result.examineId 用户任务主键id
     * @apiSuccess (success 200) {String} result.receiveName 接手任务用户名称
     * @apiSuccess (success 200) {String} result.receiveMobile 接手任务用户手机号
     * @apiSuccess (success 200) {String} result.receiveCardNo 接手任务用户身份证
     * @apiSuccess (success 200) {Date} result.receiveTime 接手任务时间
     * @apiSuccess (success 200) {String} result.examineRemark 接手任务用户审核备注
     * @apiSuccess (success 200) {Number} result.userJobStatus 0-领取任务，1-放弃任务
     * @apiSuccess (success 200) {Number} result.examineStatus 0-审核通过，1-审核不通过
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *         [{
     *             "jid": "R180800001",
     *             "name": "测试",
     *             "subhead": "测试二号",
     *             "startTime": null,
     *             "endTime": null,
     *             "address": "智慧路13号",
     *             "tags": "",
     *             "description": "任务描述",
     *             "price": 22,
     *             "status": 1,
     *             "createTime": 1533895281000,
     *             "modifyTime": 1533896122000,
     *             "isDelete": null,
     *             "examineId": 11,
     *             "receiveName": null,
     *             "receiveMobile": null,
     *             "receiveCardNo": null,
     *             "receiveTime": null,
     *             "examineRemark": null,
     *             "userJobStatus": 0
     *         }],
     *
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
     *      "message":"获取任务管理信息失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-userJob")
    @ResponseBody
    public  Map<String, Object> getUserJobByUserId(HttpServletRequest request, int rows, int page,Integer uid) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        Pagination pagination = null;
        List<Job> jobList = new ArrayList<>();
        try {
            Map<String, Object> map = new HashMap<>();
            pagination = new Pagination(rows, page);
            map.put("pagination", pagination);

            map.put("uid", uid);

            jobList = jobService.listPageSelectUserJob(map);
            resp.put("result",jobList);
            code=200;
            msg = "ok";
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "获取任务管理信息失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }


    /**
     * @api {get} /userApi/abandon-userJob New放弃任务
     * @apiDescription 根据任务的主键id修改数据库数据，放弃任务
     * @apiName abandonUserJob
     * @apiGroup user
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} examineId 用户任务主键ID，必填
     *
     * @apiSampleRequest /userApi/abandon-userJob
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":null,
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-放弃任务失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"放弃任务失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/abandon-userJob")
    @ResponseBody
    public  Map<String, Object> abandonUserJob(HttpServletRequest request,Integer examineId) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        try {
            UserJob userJob = userJobService.get(examineId);
            userJob.setStatus(Constant.JOB_DISCARD_STATUS);
            userJob.setLoseTime(new Date());
            userJobService.updateByPrimaryKeySelective(userJob);
            Job job = jobService.get(userJob.getJobId());
            job.setStatus(Constant.JOB_STATUS);
            jobService.updateByPrimaryKeySelective(job);

            resp.put("result","放弃任务成功");
            code=200;
            msg = "ok";
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "放弃任务失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }

    /**
     * @api {get} /userApi/share_job New分享加积分
     * @apiDescription 根据用户的主键id，分享加积分
     * @apiName shareJob
     * @apiGroup user
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户主键ID，必填
     *
     * @apiSampleRequest /userApi/share_job
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":null,
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 500-分享任务加积分失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"分享加积分失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/share_job")
    @ResponseBody
    public  Map<String, Object> shareJob(Integer uid) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        try {
            Map<String, Object> point = new HashMap<>();
            point.put("uid", uid);
            point.put("level","userShare");
            userService.updatePoint(point);

            resp.put("result","分享添加积分成功");
            code=200;
            msg = "ok";

        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "分享添加积分失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }

    /**
     * @api {get} /userApi/abandon-userJob2 New放弃任务2(h5端)
     * @apiDescription 根据任务的主键id修改数据库数据，放弃任务,修改返回的参数(h5端)
     * @apiName abandonUserJob2
     * @apiGroup user
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} examineId 用户任务主键ID，必填
     *
     * @apiSampleRequest /userApi/abandon-userJob2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":"1",
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-放弃任务失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"放弃任务失败",
     *      "result":"0",
     *      "code":500
     * }
     */
    @RequestMapping("/abandon-userJob2")
    @ResponseBody
    public  Map<String, Object> abandonUserJob2(HttpServletRequest request,Integer examineId) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        try {
            UserJob userJob = userJobService.get(examineId);
            userJob.setStatus(Constant.JOB_DISCARD_STATUS);
            userJob.setLoseTime(new Date());
            userJobService.updateByPrimaryKeySelective(userJob);
            Job job = jobService.get(userJob.getJobId());
            job.setStatus(Constant.JOB_STATUS);
            jobService.updateByPrimaryKeySelective(job);

            resp.put("result","1");
            code=200;
            msg = "ok";
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            resp.put("result","0");
            msg = "放弃任务失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }





}
