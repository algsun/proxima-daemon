package com.microwise.proxima.infraredImageResolution.scheduler;

import com.microwise.proxima.bean.InfraredDVPlaceBean;
import com.microwise.proxima.service.InfraredDVPlaceService;
import org.quartz.SchedulerException;

import java.io.IOException;
import java.util.List;

/**
 * 红外图片新区域处理任务计划执行程序
 * 
 * @author zhang.licong
 * @date 2012-9-6
 * 
 * @check guo.tian li.jianfei 2012-09-19
 */
public class InitNewRegionsJobBean {

	/** 负责红外图片解析任务计划添加、取消  */
	private NewRegionsScheduler newRegionsScheduler;
	/**红外热像仪点位信息服务层  */
	private InfraredDVPlaceService infraredDVPlaceService;

	public InitNewRegionsJobBean(InfraredDVPlaceService infraredDVPlaceService,
			NewRegionsScheduler newRegionsScheduler) throws SchedulerException {
		this.infraredDVPlaceService = infraredDVPlaceService;
		this.newRegionsScheduler = newRegionsScheduler;
	}

	@SuppressWarnings("unused")
    private void initNewRegionsSync() throws SchedulerException, IOException {
		// 获取摄像机点位信息
		List<InfraredDVPlaceBean> infDVPlaceList = this.infraredDVPlaceService
				.findAll();

		// 根据摄像机点位添加任务计划
		for (InfraredDVPlaceBean infraredDVPlaceBean : infDVPlaceList) {

			// 添加job
			newRegionsScheduler.addInfraredAnalyzerJob(infraredDVPlaceBean
					.getId());

			// 添加trigger
			newRegionsScheduler.addInfraredAnalyzerTrigger(infraredDVPlaceBean);
		}
	}
}
