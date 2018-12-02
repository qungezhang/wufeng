package com.planet.job.controller;

import com.planet.admin.domain.Admin;
import com.planet.common.Constant;
import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.common.util.DateTools;
import com.planet.jiguang.JiGService;
import com.planet.job.domain.Job;
import com.planet.job.domain.UserJob;
import com.planet.job.service.JobService;
import com.planet.job.service.UserJobService;
import com.planet.user.domain.UserAgent;
import com.planet.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author aiveily
 * @ClassName:JobController
 * @Description:任务接口类
 * @date 2018/8/9
 */
@Controller
@RequestMapping("/job")
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;
    @Autowired
    private UserJobService userJobService;

    @Autowired
    private UserService userService;


    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("/job/jobList");
    }

    /**
     * @Description 查询任务列表
     * @author aiveily
     * @create 2018/8/13 11:13
     */
    @RequestMapping("/listJob")
    @ResponseBody
    public Map listJob(HttpServletRequest request, int rows, int page) {
        //init
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<String, Object>();
        Pagination pagination = null;
        List<Job> jobList = new ArrayList<>();

        String jid = null; String name = null;
        String status = null;

        try {
            pagination = new Pagination(rows, page);
            map.put("pagination", pagination);

            jid = request.getParameter("jid");
            name = request.getParameter("name");
            status = request.getParameter("status");
            map.put("jid", jid);
            map.put("name", name);
            map.put("status", status);

            jobList = jobService.listPageSelectJobAll(map);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }


        model.put("rows", jobList);
        model.put("total", pagination.getCount());
        return model;
    }

    /**
     * @Description 新增任务
     * @author aiveily
     * @create 2018/8/10 11:11
     */
    @RequestMapping("/addJob")
    @ResponseBody
    public Map addJob(HttpServletRequest request) {

        //init
        Map<String, Object> model = new HashMap<>();
        String msg = "添加失败";
        int success = 0;
        String name = null;
        String subhead = null;
        String startTime = null;
        String endTime = null;
        String employerName = null;
        String employerMobile = null;
        String address = null;
        String tags = null;
        String description = null;
        String price = null;

        try {
            name = request.getParameter("name");
            subhead = request.getParameter("subhead");
            startTime = request.getParameter("startTime");
            endTime = request.getParameter("endTime");
            employerName = request.getParameter("employerName");
            employerMobile = request.getParameter("employerMobile");
            address = request.getParameter("address");
            tags = request.getParameter("tags");
            description = request.getParameter("description");
            price = request.getParameter("price");

            Job job = new Job();
            job.setName(name);
            job.setSubhead(subhead);
            job.setEmployerName(employerName);
            job.setEmployerMobile(employerMobile);
            job.setAddress(address);
            job.setStartTime(DateTools.parseStringToDate(startTime,"yyyy-MM-dd HH:mm:ss"));
            job.setEndTime(DateTools.parseStringToDate(endTime, "yyyy-MM-dd HH:mm:ss"));
            job.setTags(tags);
            job.setDescription(description);
            job.setPrice(new BigDecimal(price));

            String jid = jobService.insertByPrimaryKeySelective(job);
            if (null != jid) {
                logger.info("新增了一个任务");
                msg = "新增任务成功";
                Map<String,Object> message = new HashMap<>();
                message.put("type",Constant.JOB_STATUS);
                message.put("jobId",jid);
                message.put("jobName",job.getName());
                JiGService.sendJiGMessage(message);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            success = 0;
        }

        model.put("msg", msg);
        model.put("success", success);
        return model;
    }

    /**
     * @Description 修改任务
     * @author aiveily
     * @create 2018/8/10 11:11
     */
    @RequestMapping("/editJob")
    @ResponseBody
    public Map editJob(HttpServletRequest request) {

        //init
        Map<String, Object> model = new HashMap<>();
        String msg = "修改失败";
        int success = 0;
        String id = null;
        String name = null;
        String subhead = null;
        String startTime = null;
        String endTime = null;
        String employerName = null;
        String employerMobile = null;
        String address = null;
        String tags = null;
        String description = null;
        String price = null;

        try {
            id = request.getParameter("jid");
            name = request.getParameter("name");
            subhead = request.getParameter("subhead");
            startTime = request.getParameter("startTime");
            endTime = request.getParameter("endTime");
            employerName = request.getParameter("employerName");
            employerMobile = request.getParameter("employerMobile");
            address = request.getParameter("address");
            tags = request.getParameter("tags");
            description = request.getParameter("description");
            price = request.getParameter("price");

            Job job = new Job();
            job.setJid(id);
            job.setName(name);
            job.setSubhead(subhead);
            job.setStartTime(DateTools.parseStringToDate(startTime, "yyyy-MM-dd HH:mm:ss"));
            job.setEndTime(DateTools.parseStringToDate(endTime, "yyyy-MM-dd HH:mm:ss"));
            job.setEmployerName(employerName);
            job.setEmployerMobile(employerMobile);
            job.setAddress(address);
            job.setTags(tags);
            job.setDescription(description);
            job.setPrice(new BigDecimal(price));

            success = jobService.updateByPrimaryKeySelective(job);
            logger.info("修改了一个任务");
            if (success == 1) {
                msg = "修改任务成功";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            success = 0;
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }


    /**
     * @Description 对任务的操作
     * @author aiveily
     * @create 2018/8/23 14:51
     */
    @RequestMapping("/operationJob")
    @ResponseBody
    public Map operationJob(HttpServletRequest request) {
        //init
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        int success = 0;
        String msg = "操作失败";
        String jid = null;
        String userJobId = null;
        String status = null;

        try {
            String operationType = request.getParameter("type");
            jid = request.getParameter("jid");
            switch (operationType){
                case "del":
                    if(null != userJobService.findByReceive(jid)){
                        model.put("msg", "该任务已被领取无法删除");
                        model.put("success", success);
                        return model;
                    }
                    success = jobService.deleteByPrimaryKey(jid);
                    if (success == 1) {
                        userJobService.deleteByJid(jid);
                        msg = "删除成功";
                    }
                    break;
                case "close":
                    Job job_colse = new Job();
                    job_colse.setJid(jid);
                    job_colse.setStatus(Constant.JOB_STATUS_04);  //任务关闭
                    success = jobService.updateByPrimaryKeySelective(job_colse);
                    msg = "下架成功";
                    break;
                case "reshelf":
                    Job job_reshelf = new Job();
                    job_reshelf.setJid(jid);
                    job_reshelf.setStatus(Constant.JOB_STATUS);  //重新上架
                    success = jobService.updateByPrimaryKeySelective(job_reshelf);
                    if (success == 1) {
                        msg = "重新上架成功";
                    }
                    break;
                case "finish":
                    Job job_finish = new Job();
                    job_finish.setJid(jid);
                    job_finish.setStatus(Constant.JOB_STATUS_05);  //任务完成
                    success = jobService.updateByPrimaryKeySelective(job_finish);
                    if (success == 1) {
                        UserJob userJob = userJobService.findByReceive(jid);
                        UserAgent userAgent = userService.selectByPrimaryKey(userJob.getUid());
                        Integer num = userAgent.getServiceNum();
                        if(num == null){
                            userAgent.setServiceNum(1);
                        }else{
                            userAgent.setServiceNum( userAgent.getServiceNum() + 1);
                        }
                        userService.updateByPrimaryKey(userAgent);
                        msg = "任务已完成";
                    }
                    break;
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            success = 0;
            msg = "操作失败";
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }


}
