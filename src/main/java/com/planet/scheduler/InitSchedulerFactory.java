package com.planet.scheduler;

import com.planet.common.Constant;
import com.planet.common.util.DateTools;
import com.planet.point.domain.PointBalloon;
import com.planet.point.service.PointBalloonService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.*;


/**
 * 启动并初始化定时器
 */
public class InitSchedulerFactory extends SchedulerFactoryBean {

	@Autowired
	PointBalloonService pointBalloonService;

	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		System.out.println("init quanzt success.");

		Scheduler scheduler = this.getScheduler();

		Map<String,Object> map = new HashMap<>();
		map.put("type",1);
		map.put("getWay", Constant.POINT_BALLOON_BRAND);
		// 查询所有创建时间不为空的类型为品牌的能量数据
		List<PointBalloon> list = pointBalloonService.getListCreateTimeNotNull(map);
		if (null != list && list.size() > 0) {
			// 循环出品牌能量数据，以每条数据开始的时间作为定时任务时间，加入任务处理
			for (int i = 0; i < list.size(); i++) {
				// 根据数据库中的时间获取cron表达式
				String cron = DateTools.getCronByPatternDate(list.get(i).getCreateTime());
				JobDetail brandDetail = JobBuilder.newJob(BrandEnergyJob.class).withIdentity("balloon-0000" + (i+1), "balloon" + (i+1)).build();
				CronTrigger brandTrigger = (CronTrigger) TriggerBuilder.newTrigger()
						.withSchedule(CronScheduleBuilder.cronSchedule(cron))
						.build();
				// 将该数据的id传入任务
				brandDetail.getJobDataMap().put("id",list.get(i).getId());
				scheduler.scheduleJob(brandDetail, brandTrigger);
			}
		}

		/*** 凌晨24时清空生成的能量球 ****/
		String productCleanCron = DateTools.getCronByPatternDate(DateTools.getTimesnight());
		JobDetail productCleanDetail = JobBuilder.newJob(BrandCleanJob.class).withIdentity("balloon-000021", "balloon21").build();
		CronTrigger productCleanTrigger = (CronTrigger) TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule(productCleanCron))
				.build();
		scheduler.scheduleJob(productCleanDetail, productCleanTrigger);


		/*** 清除已截止的任务 ****/
		JobDetail jobCleanDetail = JobBuilder.newJob(JobCleanJob.class).withIdentity("balloon-000022", "balloon22").build();
		CronTrigger jobCleanTrigger = (CronTrigger) TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * * * ?"))
				.build();
		scheduler.scheduleJob(jobCleanDetail, jobCleanTrigger);

		scheduler.start();

	}
}
