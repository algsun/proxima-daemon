package com.microwise.proxima.infraredImageResolution.scheduler;

import com.microwise.proxima.bean.InfraredDVPlaceBean;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 红外摄像机点位新区域计算job
 *
 * @author zhang.licong
 * @date 2012-9-6
 * @check guo.tian li.jianfei 2012-09-19
 */
@SuppressWarnings("deprecation")
public class NewRegionsCalculateJob implements StatefulJob, InterruptableJob {

    /**
     * 实例一个记录器
     */
    public static final Logger log = LoggerFactory.getLogger(NewRegionsCalculateJob.class);

    /**
     * 自动更新boolean
     */
    private AtomicBoolean interrupted = new AtomicBoolean(false);

    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        log.info("开始分析新区域");
        ApplicationContext applicationContext = (ApplicationContext) jobExecutionContext
                .getTrigger().getJobDataMap().get("applicationContext");
        InfraredDVPlaceBean dvPlace = (InfraredDVPlaceBean) jobExecutionContext
                .getTrigger().getJobDataMap().get("dvPlace");
        NewRegionsCalculateMain newRegion = applicationContext
                .getBean(NewRegionsCalculateMain.class);
        try {
            newRegion.mainExecutive(dvPlace, interrupted);
        } catch (Exception e) {
            log.error("分析区域", e);
        }

    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        interrupted.set(true);

    }
}
