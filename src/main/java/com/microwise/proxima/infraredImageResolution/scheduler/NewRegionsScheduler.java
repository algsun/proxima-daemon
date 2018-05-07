package com.microwise.proxima.infraredImageResolution.scheduler;

import com.microwise.proxima.bean.InfraredDVPlaceBean;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 负责红外图片解析任务计划添加、取消
 *
 * @author zhang.licong
 * @date 2012-9-10
 * @check guo.tian li.jianfei 2012-09-19
 */
public class NewRegionsScheduler implements ApplicationContextAware {

    /**
     * 实例一个记录器
     */
    public final Logger log = LoggerFactory.getLogger(this.getClass());

    private Scheduler scheduler;
    private int scanTime; // 执行时间
    private ApplicationContext appCxt;//上下文

    public NewRegionsScheduler(Scheduler scheduler, int scanTime) {
        this.scheduler = scheduler;
        this.scanTime = scanTime;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.appCxt = applicationContext;

    }

    /**
     * 添加任务计划
     *
     * @param dvPlace 摄像机点位Bean
     * @author zhang.licong
     * @date 2012-9-11
     */
    public void addInfraredAnalyzerTrigger(InfraredDVPlaceBean dvPlace)
            throws SchedulerException {
        JobKey jobKey = getJobKey(dvPlace.getId());
        TriggerKey triggerKey = triggerKey(dvPlace.getId());
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("applicationContext", appCxt);
        jobDataMap.put("dvPlace", dvPlace);

        // 设置执行时间
        Trigger simpleTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInMinutes(scanTime).repeatForever())
                .usingJobData(jobDataMap).forJob(jobKey).build();

        scheduler.scheduleJob(simpleTrigger);
    }

    /**
     * 添加job
     *
     * @param dvPlaceId 摄像机点位Id
     * @throws org.quartz.SchedulerException
     * @author zhang.licong
     * @date 2012-9-11
     */
    public void addInfraredAnalyzerJob(String dvPlaceId) throws SchedulerException {
        JobDetail job = getJob(dvPlaceId);
        scheduler.addJob(job, false);
    }

    /**
     * 获取job
     *
     * @param dvPlaceId 摄像机点位ID
     * @return JobDetail
     * @author zhang.licong
     * @date 2012-9-12
     */
    private JobDetail getJob(String dvPlaceId) {
        JobKey jobKey = getJobKey(dvPlaceId);
        JobDetail job = JobBuilder.newJob(NewRegionsCalculateJob.class)
                .withIdentity(jobKey).storeDurably().build();
        return job;
    }

    /**
     * 获取jobkey
     *
     * @param dvPlaceId 摄像机点位ID
     * @return
     * @author zhang.licong
     * @date 2012-9-11
     */
    private JobKey getJobKey(String dvPlaceId) {
        return new JobKey("newRegionJob" + dvPlaceId, "newRegionGroup"
                + dvPlaceId);
    }

    /**
     * 返回 红外图片区域解析计划触发的 groupName
     *
     * @param dvPlaceId 摄像机点位ID
     * @return String
     */
    private String triggerGroupName(String dvPlaceId) {
        return String.format("newRegionsSchedule%s", dvPlaceId);
    }

    /**
     * 返回 红外图片区域解析计划触发器的 name
     *
     * @param dvPlaceId 摄像机点位ID
     * @return String
     */
    private String triggerName(String dvPlaceId) {
        return String.format("newRegionsSchedule%s", dvPlaceId);
    }

    /**
     * 返回 红外图片区域解析计划 trigger 的 key
     *
     * @param dvPlaceId 摄像机点位ID
     * @return TriggerKey
     */
    private TriggerKey triggerKey(String dvPlaceId) {
        String triggerGroupName = triggerGroupName(dvPlaceId);
        String triggerName = triggerName(dvPlaceId);
        return new TriggerKey(triggerName, triggerGroupName);
    }

}
