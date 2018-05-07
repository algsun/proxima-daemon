package com.microwise.proxima.photograph;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.OpticsDVPlaceBean;
import com.microwise.proxima.util.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 拍照工作, 由触器去触发
 *
 * @author gaohui
 * @date 2012-8-7
 * @check @gaohui #2207 2013-03-29
 */
public class PhotographJob implements Job {

    public static final Logger log = LoggerFactory.getLogger(PhotographJob.class);

    @Override
    public void execute(JobExecutionContext jobContext)
            throws JobExecutionException {
        ApplicationContext applicationContext = (ApplicationContext) jobContext
                .getTrigger().getJobDataMap()
                .get(PhotographScheduler.JOB_DATA_APP_CONTEXT);
        OpticsDVPlaceBean dvPlace = (OpticsDVPlaceBean) jobContext.getTrigger()
                .getJobDataMap().get(PhotographScheduler.JOB_DATA_DV_PLACE);
        log.debug("[{}#{}]点位执行照像", dvPlace.getPlaceName(), dvPlace.getId());

        // 将周期计划，转换为下一个拍照的时间点(不区分周期拍照，还是时间点拍照)
        try {
            DateTime deadlineDateTime = deadlineDateTime(jobContext.getScheduler(), jobContext.getTrigger(), dvPlace);
            Date deadline = deadlineDateTime.toDate();
            Object[] args = new Object[]{dvPlace.getPlaceName(), dvPlace.getId(), DateUtil.format(deadline, DateUtil.YYYY_MM_DD_HH_MM_SS)};
            log.debug("[{}#{}]最后尝试时间为:{}", args);

            RemoteIOMain main = applicationContext.getBean(RemoteIOMain.class);

            //控制 io 模块执行程序
            main.executeMain(dvPlace, deadline);
        } catch (SchedulerException e) {
            throw new JobExecutionException(e);
        } finally {
            log.debug("[{}#{}]点位退出照像", dvPlace.getPlaceName(), dvPlace.getId());
        }
    }


    /**
     * 连接失败，尝试重连的最后期限.
     * 日前的策略是当前时间到下一次拍照时间的中间点，为重连的最后期限。
     *
     * @param scheduler 任务计划
     * @param trigger   任务触发器
     * @param dvPlace   摄像机
     * @return deadlineDateTime
     * 重连最后时间
     * @throws org.quartz.SchedulerException
     */
    private DateTime deadlineDateTime(Scheduler scheduler, Trigger trigger, DVPlaceBean dvPlace) throws SchedulerException {
        //当前拍照时间
        Date fireTime = trigger.getPreviousFireTime();
        //下一次拍照时间
        Date nextTime = nextPhotographTime(scheduler, dvPlace);

        DateTime startDateTime = DateTime.now().withMillis(fireTime.getTime());
        DateTime endDateTime = DateTime.now().withMillis(nextTime.getTime());
        Duration duration = new Duration(startDateTime, endDateTime);

        long seconds = duration.getStandardSeconds() / 2;

        DateTime deadlineDateTime = startDateTime.plusSeconds((int) seconds);
        return deadlineDateTime;
    }

    /**
     * 此摄像机点位的下一次拍照时间
     *
     * @param scheduler 任务计划
     * @param dvPlace   摄像机点位对象
     * @throws org.quartz.SchedulerException
     * @author gaohui
     * @date 2012-11-16
     */
    private Date nextPhotographTime(Scheduler scheduler, DVPlaceBean dvPlace) throws SchedulerException {
        // 获取关于这个摄像机点位的所有 trigger ，然后得到他们的下一次拍照时间，然后进行排序，得到最小的时间点.
        String photographGroup = PhotographScheduler.triggerGroupName(dvPlace.getPlaceCode());
        GroupMatcher<TriggerKey> photographGroupMatcher = GroupMatcher.triggerGroupEquals(photographGroup);

        SortedSet<Date> dates = new TreeSet<Date>();

        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(photographGroupMatcher);
        for (TriggerKey triggerKey : triggerKeys) {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            dates.add(trigger.getNextFireTime());
        }
        if (dates.isEmpty()) {
            return null;
        }
        return dates.first();
    }

}
