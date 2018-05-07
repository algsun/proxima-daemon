package com.microwise.proxima.photograph;

import com.microwise.proxima.bean.OpticsDVPlaceBean;
import com.microwise.proxima.bean.PhotographScheduleBean;
import com.microwise.proxima.service.OpticsDVPlaceService;
import com.microwise.proxima.service.PhotographScheduleService;
import com.microwise.proxima.util.Configs;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.util.List;

/**
 * 拍照计划初始化
 *
 * @author gaohui
 * @date 2012-8-7
 * @check @gaohui #2294 2013-03-29
 */
public class PhotographJobInit implements ApplicationContextAware {

    public static final int PAGE_SIZE = 100; // 批量添加摄像机数目

    public static final Logger log = LoggerFactory.getLogger(PhotographJobInit.class);
    private ApplicationContext applicationContext;
    private Scheduler scheduler;
    private PhotographScheduler photographScheduler;


    public PhotographJobInit(Scheduler scheduler, PhotographScheduler photographScheduler) throws SchedulerException {
        this.scheduler = scheduler;
        this.photographScheduler = photographScheduler;

    }

    // 初始化拍照任务和触发器
    @SuppressWarnings("unused")
    private void initScheduler() throws SchedulerException, IOException {
        log.info("拍照计划开启");

        JobDetail photographJob = JobBuilder.newJob(PhotographJob.class)
                .withIdentity(PhotographScheduler.PHOTOGRAPH_JOB_KEY).storeDurably()
                .build();

        scheduler.addJob(photographJob, false);

        //初始化所有摄像机点位的拍照计划
        OpticsDVPlaceService opticsDVPlaceService = applicationContext.getBean(OpticsDVPlaceService.class);
        PhotographScheduleService photographScheduleService = applicationContext.getBean(PhotographScheduleService.class);

        // TODO 可分页查询, 考虑可能摄像机太多 @gaohui 2013-03-29
        // 获取启用光学摄像机 总数量
        Long count = opticsDVPlaceService.findAllEnableCount();

        // 批量加载 摄像机 拍照计划
        for (int i = 0; i * PAGE_SIZE < count; i++) {
            List<OpticsDVPlaceBean> optList = opticsDVPlaceService.findEnableByPage(i, PAGE_SIZE);
            for (OpticsDVPlaceBean opt : optList) {
                //获取摄像机的拍照计划
                List<PhotographScheduleBean> pssList = photographScheduleService.findAllOfDVPlace(opt.getId());
                //添加任务计划
                photographScheduler.addAllTrigger(opt, pssList);
                log.info("添加任务：" + opt.getPlaceCode());
            }
        }
        initRemoteIO();
    }

    /**
     * 初始化 IO模块服务端
     *
     * @throws java.io.IOException
     */
    private void initRemoteIO() throws IOException {
        // 获取本地是客户端还是服务端，0、客户端 1、服务端
        int mode = Integer.parseInt(Configs.get("remoteIO.mode"));

        //如果是本地是服务端, 初始化 IO模块服务端
        if (mode == RemoteIOMain.SERVER_MODE_INT) {
            int serverPort = Integer.parseInt(Configs.get("remoteIO.serverPort"));
            RemoteIOServerHolder.getInstance().initPort(serverPort);
            // 显示初始化一次
            RemoteIOServerHolder.getInstance().getRemoteIOServer();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;

    }
}
