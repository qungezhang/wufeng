package com.planet.job.controller;

import com.planet.admin.domain.Admin;
import com.planet.job.domain.UserJob;
import com.planet.job.service.UserJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aiveily
 * @ClassName:UserJobController
 * @Description:用户的任务接口
 * @date 2018/8/10
 */
@Controller
@RequestMapping("/userJob")
public class UserJobController {

    private static final Logger logger = LoggerFactory.getLogger(UserJobController.class);

    @Autowired
    private UserJobService userJobService;

    /**
     * @Description 审核任务
     * @author aiveily
     * @create 2018/8/10 11:11
     */
    @RequestMapping("/examineUserJob")
    @ResponseBody
    public Map examineUserJob(HttpServletRequest request) {

        //init
        Map<String, Object> model = new HashMap<>();
        String msg = "审核失败";
        int success = 0;
        String id = null;
        String jid = null;
        String examineStatus = null;
        String examineRemark = null;

        Admin a = (Admin) request.getSession().getAttribute("admin");
        if ("0".equals(a.getRole())) {
            try {
                id = request.getParameter("examine_id");
                if(null == id || "".equals(id)){
                    model.put("msg", "没有领取记录！");
                    model.put("success", success);
                    return model;
                }
                examineStatus = request.getParameter("examineStatus");
                examineRemark = request.getParameter("examineRemark");

                UserJob userJob = userJobService.get(Integer.parseInt(id));
                userJob.setExamineStatus(Integer.parseInt(examineStatus));
                userJob.setExamineRemark(examineRemark);
                userJob.setExamineTime(new Date());

                success = userJobService.examineUserJob(userJob);
                logger.info("审核成功");
                if (success == 1) {
                    msg = "审核成功";
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                success = 0;
            }
        } else {
            msg = "权限错误！";
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }
}
