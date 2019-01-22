package com.planet.job.controller;

import com.planet.common.Constant;
import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.job.domain.Job;
import com.planet.job.domain.UserJob;
import com.planet.job.service.JobService;
import com.planet.job.service.UserJobService;
import com.planet.user.domain.UserAgent;
import com.planet.user.service.UserService;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author winnie
 * @ClassName:JobApiController
 * @Description:任务管理接口类
 * @date 2018/8/10
 */
@Controller
@RequestMapping("/jobApi")
public class JobApiController {

    private static final Logger logger = LoggerFactory.getLogger(JobApiController.class);

    @Autowired
    private JobService jobService;
    @Autowired
    private UserJobService userJobService;
    @Autowired
    private UserService userService;


    /**
     * @api {get} /jobApi/get-job New获取任务列表
     * @apiDescription 根据后台发布的任务列表
     * @apiName getJob
     * @apiGroup job
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} rows 当前页数据个数，必填
     * @apiParam {Number} page 当前页数，必填
     *
     * @apiSampleRequest /jobApi/get-job
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
     * @apiSuccess (success 200) {Number} result.status  状态：0 待领取；1 待审核；2 审核通过；3 任务完成；4 审核未通过；5 任务关闭
     * @apiSuccess (success 200) {Date} result.createTime 创建时间
     * @apiSuccess (success 200) {Date} result.modifyTime 修改时间
     * @apiSuccess (success 200) {Number} result.isDelete 删除判断
     * @apiSuccess (success 200) {Number} result.examineId 用户任务主键id
     * @apiSuccess (success 200) {Number} result.receiveId 接收任务的用户id
     * @apiSuccess (success 200) {Number} result.receiveName 接收任务用户名称
     * @apiSuccess (success 200) {String} result.receiveMobile 接收任务用户手机号
     * @apiSuccess (success 200) {String} result.receiveCardNo 接收任务用户身份证
     * @apiSuccess (success 200) {Date} result.receiveTime 接收任务时间
     * @apiSuccess (success 200) {String} result.examineRemark 接收任务用户审核备注
     * @apiSuccess (success 200) {Number} result.userJobStatus 0-领取任务，1-放弃任务
     * @apiSuccess (success 200) {String} result.district 任务区域
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *         [{
     *              "jid": "R180800001",
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
     *             "receiveId":824,
     *             "receiveName": null,
     *             "receiveMobile": null,
     *             "receiveCardNo": null,
     *             "receiveTime": null,
     *             "examineRemark": null,
     *             "userJobStatus": null,
     *             "district": "无锡市惠山区"
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
     *      "message":"获取任务列表信息失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-job")
    @ResponseBody
    public Map<String, Object> getJob(int rows, int page) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;
        Pagination pagination = null;
        List<Job> jobList = new ArrayList<>();
        String jid = null;
        String name = null;
        Integer status = Constant.JOB_STATUS;

        try {
            Map<String, Object> map = new HashMap<>();
            pagination = new Pagination(rows, page);
            map.put("pagination", pagination);

            map.put("jid", jid);
            map.put("name", name);
            map.put("status", status);

            jobList = jobService.listPageSelectJob(map);
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
     * @api {get} /jobApi/get-jobDetail New获取任务详情
     * @apiDescription 根据任务主键ID获取任务详情
     * @apiName getJobDetail
     * @apiGroup job
     * @apiVersion 2.0.0
     *
     * @apiParam {String} jobId 任务主键ID，必填
     *
     * @apiSampleRequest /jobApi/get-jobDetail
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容
     * @apiSuccess (success 200) {String} result.name 任务名称
     * @apiSuccess (success 200) {String} result.subhead 副标题
     * @apiSuccess (success 200) {Date} result.startTime 开始时间
     * @apiSuccess (success 200) {String} result.address 地址
     * @apiSuccess (success 200) {String} result.tags 标签
     * @apiSuccess (success 200) {String} result.description 内容
     * @apiSuccess (success 200) {BigDecimal} result.price 价格
     * @apiSuccess (success 200) {Number} result.status  状态：0 待领取；1 待审核；2 审核通过；3 任务完成；4 审核未通过；5 任务关闭
     * @apiSuccess (success 200) {Date} result.createTime 创建时间
     * @apiSuccess (success 200) {Date} result.modifyTime 修改时间
     * @apiSuccess (success 200) {Number} result.isDelete 删除判断
     * @apiSuccess (success 200) {Number} result.examineId 用户任务主键id
     * @apiSuccess (success 200) {Number} result.receiveId 接收任务的用户id
     * @apiSuccess (success 200) {Number} result.receiveName 接手任务用户名称
     * @apiSuccess (success 200) {String} result.receiveMobile 接手任务用户手机号
     * @apiSuccess (success 200) {String} result.receiveCardNo 接手任务用户身份证
     * @apiSuccess (success 200) {Date} result.receiveTime 接手任务时间
     * @apiSuccess (success 200) {String} result.examineRemark 接手任务用户审核备注
     * @apiSuccess (success 200) {Number} result.userJobStatus 0-领取任务，1-放弃任务
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *         {
     *              "jid": "R180800002",
     *             "name": "测试",
     *             "subhead": "测试二号",
     *             "startTime": 1534233777000,
     *             "endTime": null,
     *             "address": "智慧路13号",
     *             "tags": "",
     *             "description": "任务描述任务描述",
     *             "price": 22,
     *             "status": 0,
     *             "createTime": 1533895281000,
     *             "modifyTime": 1534233783000,
     *             "isDelete": null,
     *             "examineId": null,
     *             "receiveId":824,
     *             "receiveName": null,
     *             "receiveMobile": null,
     *             "receiveCardNo": null,
     *             "receiveTime": null,
     *             "examineRemark": null,
     *              "userJobStatus": null
     *
     *         },
     *
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-获取任务详情失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取任务详情失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-jobDetail")
    @ResponseBody
    public Map<String, Object> getJobDetail(String jobId) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;

        try {
            Job job = jobService.get(jobId);
            resp.put("result",job);
            code=200;
            msg = "ok";
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "获取任务详情失败";
        }
        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }

    /**
     * @api {get} /jobApi/get-getJob New领取任务
     * @apiDescription 领取任务
     * @apiName getGetJob
     * @apiGroup job
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 当前用户主键ID，必填
     *
     * @apiSampleRequest /jobApi/get-getJob
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息（ok）
     * @apiSuccess (success 200) {Object} result 响应内容，距离可以领取任务时间
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":null,
     *      "code":200
     * }
     * {
     *      "message":"距离上次放弃任务不满3天",
     *      "result":"1天23小时59分49秒",,
     *      "code":400
     * }
     * {
     *       "message":"该用户不是经纪人",
     *       "result":null,
     *      "code":400
     *  }
     *
     * @apiError (error) {Number} code 400-领取任务失败
     * @apiError (error) {String} message 响应信息(距离上次放弃任务不满3天，该用户不是经纪人)
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"领取任务失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-getJob")
    @ResponseBody
    public  Map<String, Object> getGetJob(HttpServletRequest request, Integer uid) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;

        try {
            UserAgent userAgent = userService.selectByPrimaryKey(uid);
            //判断用户是否是经纪人
            if(userAgent.getVip() == 1){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date endTime=new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endTime);
                calendar.add(Calendar.DAY_OF_MONTH, -3);
                Date startTime = calendar.getTime();

                Map<String, Object> map = new HashMap<>();
                map.put("uid",uid);
                map.put("startTime",startTime);
                map.put("endTime",endTime);

                List<UserJob> userJob = userJobService.getUpToDate(map);

                //判断用户是否在这3天内放弃过任务
                if(userJob != null && userJob.size()>0){
                    long day = 0;
                    long hour = 0;
                    long min = 0;
                    long sec = 0;
                    long ms = 0;


                    Calendar  endDate = Calendar.getInstance();
                    endDate.setTime(userJob.get(0).getLoseTime());
                    endDate.add(Calendar.DAY_OF_MONTH,3);

                    long time1 = new Date().getTime();
                    long time2 = endDate.getTime().getTime();

                    long diff ;
                    if(time1 < time2) {
                        diff = time2 - time1;
                    } else {
                        diff = time1 - time2;
                    }
                    day = diff / (24 * 60 * 60 * 1000);
                    hour = (diff / (60 * 60 * 1000) - day * 24);
                    min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
                    sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
                    //ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - sec * 1000);
                    String str = day + "天" + hour + "小时" + min + "分" + sec + "秒";

                    resp.put("result",str);
                    code=400;
                    msg = "距离上次放弃任务不满3天";
                }else{

                    resp.put("result",null);
                    code=200;
                    msg = "ok";
                }

            }else{
                code=400;
                msg = "该用户不是经纪人";
            }

        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "领取任务失败";
        }

        resp.put("code", code);
        resp.put("message", msg);

        return resp;

    }

    /**
     * @api {get} /jobApi/get-jobSuccess New领取任务成功，保存数据
     * @apiDescription 领取任务保存数据
     * @apiName getJobSuccess
     * @apiGroup job
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} jobId 任务主键ID，必填
     * @apiParam {Number} uid 用户ID，必填
     * @apiParam {String} username 用户名称，必填
     * @apiParam {String} mobile 用户手机号，必填
     * @apiParam {String} cardNo 用户身份证号，必填
     *
     * @apiSampleRequest /jobApi/get-getJob
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
     * @apiError (error) {Number} code 400-领取任务失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"领取任务失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-jobSuccess")
    @ResponseBody
    public Map<String, Object> getJobSuccess(HttpServletRequest request, Integer uid,String jobId,String username,String mobile,String cardNo,String remark) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;

        try {
            //判斷用戶是否領取過該任務
            Map<String,Object> map = new HashMap<>();
            map.put("jobId",jobId);
            map.put("uid",uid);
            List<UserJob> userJobListByJidAndUser = userJobService.getUserJobListByJidAndUser(map);
            if(userJobListByJidAndUser!=null&&userJobListByJidAndUser.size()>0){
                code = 500;
                msg = "领取失败，重复领取!";
            }else{
                //保存用户领取任务的数据
                //修改任务表的数据
                Job job1 = jobService.get(jobId);
                if(!job1.getStatus().equals(0)&&!job1.getStatus().equals(1)){
                    throw new RuntimeException("领取失败!!");
                }
                UserJob userJobNew = new UserJob();
                userJobNew.setCreateTime(new Date());
//                userJobNew.setCardNo(cardNo);
//                userJobNew.set
                userJobNew.setAddress(cardNo);
                userJobNew.setExamineStatus(Constant.JOB_EXAMINE_STATUS);
                userJobNew.setJobId(jobId);
                userJobNew.setUid(uid);
                userJobNew.setMobile(mobile);
                userJobNew.setUsername(username);
                userJobNew.setStatus(Constant.JOB_CLAIM_STATUS);
                userJobNew.setRemark(remark);
                userJobService.insertByPrimaryKeySelective(userJobNew);

                Job job = jobService.get(jobId);
                job.setStatus(1);
                jobService.updateByPrimaryKeySelective(job);
                resp.put("result",null);
                code=200;
                msg = "ok";
            }
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "领取任务失败";
        }

        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }

    /**
     * @api {get} /jobApi/get-getJob2 New领取任务2(h5端)
     * @apiDescription 领取任务，获取任务数据方式修改(h5端)
     * @apiName getGetJob2
     * @apiGroup job
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 当前用户主键ID，必填
     *
     * @apiSampleRequest /jobApi/get-getJob2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息（ok）
     * @apiSuccess (success 200) {Object} result 响应内容，距离可以领取任务时间
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":"0",
     *      "code":200
     * }
     * {
     *      "message":"距离上次放弃任务不满3天",
     *      "result":"1天23小时59分49秒",
     *      "code":400
     * }
     * {
     *       "message":"该用户不是经纪人",
     *       "result":"2",
     *      "code":400
     *  }
     *
     * @apiError (error) {Number} code 400-领取任务失败
     * @apiError (error) {String} message 响应信息(距离上次放弃任务不满3天，该用户不是经纪人)
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"领取任务失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/get-getJob2")
    @ResponseBody
    public  Map<String, Object> getGetJob2(HttpServletRequest request, Integer uid) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;

        try {
            UserAgent userAgent = userService.selectByPrimaryKey(uid);
            //判断用户是否是经纪人
            if(userAgent.getVip() == 1){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date endTime=new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endTime);
                calendar.add(Calendar.DAY_OF_MONTH, -3);
                Date startTime = calendar.getTime();

                Map<String, Object> map = new HashMap<>();
                map.put("uid",uid);
                map.put("startTime",startTime);
                map.put("endTime",endTime);

                List<UserJob> userJob = userJobService.getUpToDate(map);

                //判断用户是否在这3天内放弃过任务
                if(userJob != null && userJob.size()>0){
                    code=400;
                    long day = 0;
                    long hour = 0;
                    long min = 0;
                    long sec = 0;
                    long ms = 0;


                    Calendar  endDate = Calendar.getInstance();
                    endDate.setTime(userJob.get(0).getLoseTime());
                    endDate.add(Calendar.DAY_OF_MONTH,3);

                    long time1 = new Date().getTime();
                    long time2 = endDate.getTime().getTime();

                    long diff ;
                    if(time1 < time2) {
                        diff = time2 - time1;
                    } else {
                        diff = time1 - time2;
                    }
                    day = diff / (24 * 60 * 60 * 1000);
                    hour = (diff / (60 * 60 * 1000) - day * 24);
                    min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
                    sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
                    //ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - sec * 1000);
                    String str = day + "天" + hour + "小时" + min + "分" + sec + "秒";

                    resp.put("result",str);
                    msg = "距离上次放弃任务不满3天";
                }else{
                    resp.put("result","0");
                    code=200;
                    msg = "ok";
                }

            }else{
                code=400;
                resp.put("result","2");
                msg = "该用户不是经纪人";
            }

        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            msg = "领取任务失败";
        }

        resp.put("code", code);
        resp.put("message", msg);

        return resp;

    }

    /**
     * @api {get} /jobApi/get-jobSuccess2 New领取任务成功，保存数据2(h5端)
     * @apiDescription 领取任务保存数据，修改返回的参数数据(h5端)
     * @apiName getJobSuccess2
     * @apiGroup job
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} jobId 任务主键ID，必填
     * @apiParam {Number} uid 用户ID，必填
     * @apiParam {String} username 用户名称，必填
     * @apiParam {String} mobile 用户手机号，必填
     * @apiParam {String} cardNo 用户身份证号，必填
     *
     * @apiSampleRequest /jobApi/get-getJob
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
     * @apiError (error) {Number} code 400-领取任务失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"领取任务失败",
     *      "result":"0",
     *      "code":500
     * }
     */
    @RequestMapping("/get-jobSuccess2")
    @ResponseBody
    public Map<String, Object> getJobSuccess2(HttpServletRequest request, Integer uid,String jobId,String username,String mobile,String cardNo,String remark) {
        Map<String, Object> resp = new HashMap<>();
        String msg;
        int code;

        try {
            //判斷用戶是否領取過該任務
            Map<String,Object> map = new HashMap<>();
            map.put("jobId",jobId);
            map.put("uid",uid);
            List<UserJob> userJobListByJidAndUser = userJobService.getUserJobListByJidAndUser(map);
            if(userJobListByJidAndUser!=null&&userJobListByJidAndUser.size()>0){
                code = 500;
                msg = "领取失败，重复领取!";
            }else{
                //保存用户领取任务的数据
                //修改任务表的数据
                Job job1 = jobService.get(jobId);
                if(!job1.getStatus().equals(0)&&!job1.getStatus().equals(1)){
                    throw new RuntimeException("领取失败!!");
                }
                UserJob userJobNew = new UserJob();
                userJobNew.setCreateTime(new Date());
                userJobNew.setCardNo(cardNo);
                userJobNew.setExamineStatus(Constant.JOB_EXAMINE_STATUS);
                userJobNew.setJobId(jobId);
                userJobNew.setUid(uid);
                userJobNew.setMobile(mobile);
                userJobNew.setUsername(username);
                userJobNew.setStatus(Constant.JOB_CLAIM_STATUS);
                userJobNew.setRemark(remark);
                userJobService.insertByPrimaryKeySelective(userJobNew);

                Job job = jobService.get(jobId);
                job.setStatus(1);
                jobService.updateByPrimaryKeySelective(job);
                resp.put("result","1");
                code=200;
                msg = "ok";
            }
        } catch (Exception e) {
            logger.error("GetJob Error:",e);
            code = 500;
            resp.put("result","0");
            msg = "领取任务失败";
        }

        resp.put("code", code);
        resp.put("message", msg);

        return resp;
    }




}
