package com.planet.scheduler;

import com.planet.common.Constant;
import com.planet.job.domain.Job;
import com.planet.job.domain.UserJob;
import com.planet.job.service.JobService;
import com.planet.job.service.UserJobService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author aiveily
 * @ClassName:JobCleanJob
 * @Description:清除到期的任务
 *              (截止时间30分钟之内要到期的任务)
 * @date 2018/9/5
 */
@DisallowConcurrentExecution
public class JobCleanJob  implements org.quartz.Job, Serializable {

    @Autowired
    JobService jobService;

    @Autowired
    UserJobService userJobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date() + ": 清除快到期的任务!");
        try {
            List<Job> jobList = jobService.selectIn30MinuteJobList();
            if(null != jobList && jobList.size() > 0){
                for(Job j :jobList){
                    j.setStatus(Constant.JOB_STATUS_04);
                    jobService.updateByPrimaryKeySelective(j);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
