package com.microwise.proxima.photograph;

import com.microwise.proxima.bean.*;
import com.microwise.proxima.service.OpticsDVPlaceService;
import com.microwise.proxima.service.PhotographScheduleService;
import org.joda.time.DateTime;
import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 负责添加拍照计划或者取消拍照计划
 * 
 * @author gaohui
 * @date 2012-8-7
 * @author xubaoji
 * @date 2013-03-26
 * @check @gaohui #2309 2013-03-29
 */
public class PhotographScheduler implements ApplicationContextAware {
	public static final String PHOTOGRAPH_GROUP = "photographGroup";
	public static final String PHOTOGRAPH_JOB = "photographJob";
	public static final JobKey PHOTOGRAPH_JOB_KEY = new JobKey(PHOTOGRAPH_JOB,
			PHOTOGRAPH_GROUP);

	public static final String JOB_DATA_APP_CONTEXT = "applicationContext";
	public static final String JOB_DATA_DV_PLACE = "dvPlace";
	public static final String JOB_DATA_PHOTOGRAPH_SCHEDULE = "photographSchecudel";

	private Scheduler scheduler;
	private ApplicationContext appCxt;

	public PhotographScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.appCxt = applicationContext;

	}

	/**
	 * 通过摄像机编号添加摄像机的拍照计划
	 * 
	 * @param dvId
	 *            摄像机编号
	 * @throws org.quartz.SchedulerException
	 */
	public void addTriggerByDvId(String dvId) throws SchedulerException {
		OpticsDVPlaceService opticsDVPlaceService = appCxt.getBean(OpticsDVPlaceService.class);
		PhotographScheduleService photographScheduleService = appCxt.getBean(PhotographScheduleService.class);
		OpticsDVPlaceBean opticsDvp = opticsDVPlaceService.findById(dvId);
        // 启用且有外部控制
		if (opticsDvp.isEnable() && opticsDvp.isIoOn()) {
			addAllTrigger(opticsDvp, photographScheduleService.findAllOfDVPlace(dvId));
		}
	}

	/**
	 * 取消某个摄像机点位的所有拍照计划
	 * 
	 * @param dvId
	 *            摄像机编号
	 * @throws org.quartz.SchedulerException
	 */
	public void cancelTriggerByDvId(String dvId) throws SchedulerException {
		OpticsDVPlaceService opticsDVPlaceService = appCxt.getBean(OpticsDVPlaceService.class);
		String triggerGroupName = triggerGroupName(opticsDVPlaceService.findById(dvId).getPlaceCode());
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroupName));

		for (TriggerKey triggerKey : triggerKeys) {
			scheduler.unscheduleJob(triggerKey);
		}
	}

	/**
	 * 添加所有的拍照计划
	 * 
	 * @param dvPlace
	 * @param photographSchedules
	 * @throws org.quartz.SchedulerException
	 */
	public void addAllTrigger(OpticsDVPlaceBean dvPlace, List<PhotographScheduleBean> photographSchedules)
			throws SchedulerException {

		for (PhotographScheduleBean photographSchedule : photographSchedules) {
			addTrigger(dvPlace, photographSchedule);
		}
	}

	/**
	 * 
	 * 添加拍照计划
	 * 
	 * @param dvPlace
	 * @param photographSchedule
	 * @throws org.quartz.SchedulerException
	 * @throws IllegalArgumentException
	 *             如果拍照计划不支持
	 */
	public void addTrigger(OpticsDVPlaceBean dvPlace, PhotographScheduleBean photographSchedule)
			throws SchedulerException {
		
		// 分时间点 和周期 进行添加拍照计划
		if (photographSchedule instanceof PhotographPointScheduleBean) {
			addPointTrigger(dvPlace, (PhotographPointScheduleBean) photographSchedule);
		} else if (photographSchedule instanceof PhotographIntervalScheduleBean) {
			addIntervalTrigger(dvPlace, (PhotographIntervalScheduleBean) photographSchedule);
		} else {
			throw new IllegalArgumentException("添加的拍照计划暂不支持. class :"
					+ photographSchedule.getClass());
		}

	}

	/**
	 * 取消某个摄像机点位的所有拍照计划
	 * 
	 * @throws org.quartz.SchedulerException
	 */
	public void cancelAllTrigger(OpticsDVPlaceBean dvPlace)
			throws SchedulerException {
		String triggerGroupName = triggerGroupName(dvPlace.getPlaceCode());
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher
				.triggerGroupEquals(triggerGroupName));

		for (TriggerKey triggerKey : triggerKeys) {
			scheduler.unscheduleJob(triggerKey);
		}
	}

	/**
	 * 添加时间点拍照计划
	 * 
	 * @param dvPlace
	 * @param photographPointSchedule
	 * @throws org.quartz.SchedulerException
	 */
	private void addPointTrigger(OpticsDVPlaceBean dvPlace,
			PhotographPointScheduleBean photographPointSchedule)
			throws SchedulerException {
		TriggerKey triggerKey = triggerKey(dvPlace.getPlaceCode(),
				photographPointSchedule.getId());

		// 传IO参数
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(JOB_DATA_DV_PLACE, dvPlace);
		jobDataMap.put(JOB_DATA_APP_CONTEXT, appCxt);
		jobDataMap.put(JOB_DATA_PHOTOGRAPH_SCHEDULE, photographPointSchedule);

		Date timePoint = photographPointSchedule.getTimePoint();
		DateTime dateTime = DateTime.now().withMillis(timePoint.getTime());

		if (photographPointSchedule.getDayOfWeek() == DayType.ALL) {// 每天
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity(triggerKey)
					.usingJobData(jobDataMap)
					.forJob(PHOTOGRAPH_JOB_KEY)
					.withSchedule(
							CronScheduleBuilder.dailyAtHourAndMinute(
									dateTime.getHourOfDay(),
									dateTime.getMinuteOfHour())).build();

			scheduler.scheduleJob(trigger);

		} else {
			DayType dayType = photographPointSchedule.getDayOfWeek();
			int dayOfWeek = dayOfWeekFromDayType(dayType);
			addWeekPointTrigger(triggerKey, jobDataMap, dayOfWeek, dateTime);
		}

	}

	/**
	 * 添加周期拍照计划
	 * 
	 * @param dvPlace
	 * @param photographIntervalSchedule
	 * @throws org.quartz.SchedulerException
	 */
	private void addIntervalTrigger(OpticsDVPlaceBean dvPlace,
			PhotographIntervalScheduleBean photographIntervalSchedule)
			throws SchedulerException {

		TriggerKey triggerKey = triggerKey(dvPlace.getPlaceCode(),
				photographIntervalSchedule.getId());

		// 传IO参数
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(JOB_DATA_DV_PLACE, dvPlace);
		jobDataMap.put(JOB_DATA_APP_CONTEXT, appCxt);
		jobDataMap.put(JOB_DATA_PHOTOGRAPH_SCHEDULE, photographIntervalSchedule);

		Date startTime = photographIntervalSchedule.getStartTime();
		Date endTime = photographIntervalSchedule.getEndTime();
		int intervalMinutes = photographIntervalSchedule.getInterval();

		if (photographIntervalSchedule.getDayOfWeek() == DayType.ALL) {
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity(triggerKey)
					.usingJobData(jobDataMap)
					.forJob(PHOTOGRAPH_JOB_KEY)
					.withSchedule(
							DailyTimeIntervalScheduleBuilder
									.dailyTimeIntervalSchedule()
									.startingDailyAt(
											TimeOfDay
													.hourAndMinuteFromDate(startTime))
									.withInterval(intervalMinutes,
											IntervalUnit.MINUTE)
									.endingDailyAt(
											TimeOfDay
													.hourAndMinuteFromDate(endTime)))
					.build();
			scheduler.scheduleJob(trigger);
		} else {
			DayType dayType = photographIntervalSchedule.getDayOfWeek();
			int dayOfWeek = dayOfWeekFromDayType(dayType);
			addWeekIntervalTrigger(triggerKey, jobDataMap, startTime, endTime,
					intervalMinutes, dayOfWeek);
		}

	}

	/**
	 * 返回 拍照计划触发的 groupName
	 * 
	 * @param dvPlaceCode
	 * @return
	 */
	public static String triggerGroupName(String dvPlaceCode) {
		return String.format("photograph:%s", dvPlaceCode);
	}

	/**
	 * 返回 拍照计划触发器的 name
	 * 
	 * 
	 * @param photographScheduleId
	 * @return
	 */
	private String triggerName(String photographScheduleId) {
		return String.format("photographSchedule:%s", photographScheduleId);
	}

	/**
	 * 返回 拍照计划 trigger 的 key
	 * 
	 * 
	 * @param dvPlaceCode
	 * @param photographScheduleId
	 * @return
	 */
	private TriggerKey triggerKey(String dvPlaceCode,
			String photographScheduleId) {
		String triggerGroupName = triggerGroupName(dvPlaceCode);
		String triggerName = triggerName(photographScheduleId);

		return new TriggerKey(triggerName, triggerGroupName);
	}

	/**
	 * 添加7天时间点trigger
	 * 
	 * @author zhang.licong
	 * @date 2012-8-10
	 * 
	 * 
	 */
	private void addWeekPointTrigger(TriggerKey triggerKey,
			JobDataMap jobDataMap, int dateBuilder, DateTime dateTime)
			throws SchedulerException {
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(triggerKey)
				.usingJobData(jobDataMap)
				.forJob(PHOTOGRAPH_JOB_KEY)
				.withSchedule(
						CronScheduleBuilder.weeklyOnDayAndHourAndMinute(
								dateBuilder, dateTime.getHourOfDay(),
								dateTime.getMinuteOfHour())).build();
		scheduler.scheduleJob(trigger);
	}

	/**
	 * 添加7天周期trigger
	 * 
	 * @author zhang.licong
	 * @date 2012-8-10
	 * 
	 * 
	 */
	private void addWeekIntervalTrigger(TriggerKey triggerKey,
			JobDataMap jobDataMap, Date startTime, Date endTime,
			int intervalMinutes, int dayOfWeek) throws SchedulerException {
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(triggerKey)
				.usingJobData(jobDataMap)
				.forJob(PHOTOGRAPH_JOB_KEY)
				.withSchedule(
						DailyTimeIntervalScheduleBuilder
								.dailyTimeIntervalSchedule()
								.onDaysOfTheWeek(dayOfWeek)
								.startingDailyAt(
										TimeOfDay
												.hourAndMinuteFromDate(startTime))
								.withInterval(intervalMinutes,
										IntervalUnit.MINUTE)
								.endingDailyAt(
										TimeOfDay
												.hourAndMinuteFromDate(endTime)))
				.build();
		scheduler.scheduleJob(trigger);
	}

	/**
	 * 将 DayType 转换为 DateBuilder 中的星期枚举
	 * 
	 * @param dayType
	 * @return
	 */
	private static int dayOfWeekFromDayType(DayType dayType) {
		if (dayType == DayType.ALL) {
			throw new IllegalArgumentException("类型错误");
		}

		switch (dayType) {
		case MON:
			return DateBuilder.MONDAY;
		case TUES:
			return DateBuilder.TUESDAY;
		case WED:
			return DateBuilder.WEDNESDAY;
		case THURS:
			return DateBuilder.THURSDAY;
		case FRI:
			return DateBuilder.FRIDAY;
		case SAT:
			return DateBuilder.SATURDAY;
		case SUN:
			return DateBuilder.SUNDAY;
		default:
			return -1;
		}
	}
}
