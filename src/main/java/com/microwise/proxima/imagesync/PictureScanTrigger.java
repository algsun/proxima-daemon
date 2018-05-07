package com.microwise.proxima.imagesync;

import com.microwise.proxima.bean.DVPlaceBean;
import org.joda.time.DateTime;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Date;
import java.util.Random;

/**
 * 负责 图片扫描任务触发器的添加和删除
 *
 * @author gaohui
 * @date 2012-7-19
 * @check @xubaoji #2348 2013-03-29
 */
public class PictureScanTrigger implements ApplicationContextAware {
    public static final Logger log = LoggerFactory.getLogger(PictureScanTrigger.class);

    // 图片同步默认组名
    private static final String PICTURE_SCAN_DEFAULT_GROUP_NAME = "pictureScan";
    public static final String JOB_DATA_APP_CONTEXT = "applicationContext";
    public static final String JOB_DATA_DV_PLACE = "dvPlaceBean";

    private Scheduler scheduler;
    private ApplicationContext appCxt;
    // 图片扫描时间间隔(单位为分)
    private int pictureScanPeriod;

    public PictureScanTrigger(Scheduler scheduler, int pictureScanPeriod) {
        this.scheduler = scheduler;
        this.pictureScanPeriod = pictureScanPeriod;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.appCxt = applicationContext;
    }

    /**
     * 添加对某个摄像机的图片扫描
     *
     * @param dvPlace
     */
    public void addTrigger(DVPlaceBean dvPlace) {
        //如果摄像机点位未启用，则直接返回
        if (!dvPlace.isEnable()) {
            return;
        }

        try {
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(
                    InitJobBean.PICTURE_SCAN_JOB,
                    InitJobBean.PICTURE_SCAN_GROUP));
            
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(JOB_DATA_APP_CONTEXT, appCxt);
            jobDataMap.put(JOB_DATA_DV_PLACE, dvPlace);

            // 延迟随机 10秒到 60 秒 避免突发执行 @gaohui 2013-03-27
            Date startAt = DateTime.now().plusSeconds(10 + new Random().nextInt(50)).toDate();

            Trigger simpleTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(dvPlace.getPlaceCode(), PICTURE_SCAN_DEFAULT_GROUP_NAME)
                    .startAt(startAt)
                    .withSchedule(
                        SimpleScheduleBuilder
                            .simpleSchedule()
                            .withIntervalInSeconds(pictureScanPeriod * 60)
                            .repeatForever()
                    )
                    .usingJobData(jobDataMap)
                    .forJob(InitJobBean.PICTURE_SCAN_JOB,
                            InitJobBean.PICTURE_SCAN_GROUP).build();

            scheduler.scheduleJob(simpleTrigger);
        } catch (SchedulerException e) {
            log.error("添加摄像机图片同步, ID: " + dvPlace.getId(), e);
        }
    }

    /**
     * 取消某个摄像机的定时扫描
     *
     * @param dvPlace
     */
    public void cancelTrigger(DVPlaceBean dvPlace) {
        try {
            scheduler.unscheduleJob(new TriggerKey(dvPlace.getPlaceCode(), PICTURE_SCAN_DEFAULT_GROUP_NAME));
        } catch (SchedulerException e) {
            log.error("取消摄像机图片同步, ID: " + dvPlace.getId(), e);
        }
    }
}
