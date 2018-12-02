package com.planet.scheduler;

import com.planet.point.domain.PointBalloon;
import com.planet.point.service.PointBalloonService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌产生能量球
 * @author winnie
 */
@DisallowConcurrentExecution
public class BrandEnergyJob implements Job, Serializable{

	private static final long serialVersionUID = -9075321125727146693L;

	@Autowired
	private PointBalloonService pointBalloonService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println(new Date() + ": 生成类型能量球!");
		try {
//			Integer type = Constant.POINT_BALLOON_BRAND;
//			int num = pointBalloonService.getBalloonNum(type);
//			if(num < 10){
//				PointBalloon pointBalloon = pointBalloonService.randPointBalloon(type);
//				pointBalloon.setEnergyStatus(1);
//				pointBalloonService.updateByPrimaryKey(pointBalloon);
//			}
			JobDataMap dataMap = arg0.getJobDetail().getJobDataMap();
			Integer id = dataMap.getInt("id");
			PointBalloon pointBalloon = pointBalloonService.get(id);
			if (null != pointBalloon) {
				pointBalloon.setEnergyStatus(1);
				pointBalloonService.updateByPrimaryKey(pointBalloon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
