package com.planet.scheduler;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 * 实现JobFactory 接口，在实例化Job以后，在通过 ApplicationContext 将Job所需要的属性注入
 * @author jiangdw
 */
public class JobFactory extends AdaptableJobFactory {

	// 这个对象Spring会帮我们自动注入进来,也属于Spring技术范畴.
	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;

	protected Object createJobInstance(TriggerFiredBundle bundle)
			throws Exception {
		// 调用父类的方法
		Object jobInstance = super.createJobInstance(bundle);
		// 进行注入,这属于Spring的技术,不清楚的可以查看Spring的API.
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}