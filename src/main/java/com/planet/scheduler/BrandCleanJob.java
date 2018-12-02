package com.planet.scheduler;

import com.planet.common.Constant;
import com.planet.point.service.PointBalloonService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;

/**
 * @author aiveily
 * @ClassName:BrandCleanJob
 * @Description:清除品牌气泡的定时任务
 * @date 2018/8/9
 */
@DisallowConcurrentExecution
public class BrandCleanJob implements Job, Serializable {

    private static final long serialVersionUID = -9075321125727146693L;

    @Autowired
    private PointBalloonService pointBalloonService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(new Date() + ": 清除类型能量球!");
        try {
            Integer type = Constant.POINT_BALLOON_BRAND;
            pointBalloonService.updateEnergyStatus(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
