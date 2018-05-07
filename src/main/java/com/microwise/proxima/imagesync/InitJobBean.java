package com.microwise.proxima.imagesync;

import com.google.common.base.Strings;
import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.service.DVPlaceService;
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
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Collection;

/**
 * 任务计划执行程序
 *
 * @author zhang.licong
 * @author gaohui
 * @date 2012-7-6
 * @date 2013-03-22
 * @check @xubaoji #2293  2013-03-29
 */
public class InitJobBean implements ApplicationContextAware {
    public static final Logger log = LoggerFactory.getLogger(InitJobBean.class);


    public static final String PICTURE_SCAN_JOB = "pictureScanJob";
    public static final String PICTURE_SCAN_GROUP = "pictureScanGroup";
    public static final int BATCH_SIZE = 100; // 批量添加摄像机数目

    private PictureScanTrigger pictureScanTrigger;

    private Scheduler scheduler;
    private ApplicationContext applicationContext;
    // 用所在文件系统中的位置
    private String webAppBasePath;
    // 图片资源根目录 (全局变量)
    public static String imageResourcesRootDirPath;
    public static File imageResourcesRootDir;

    public InitJobBean(Scheduler scheduler, PictureScanTrigger pictureScanTrigger)
            throws SchedulerException {
        this.scheduler = scheduler;
        this.pictureScanTrigger = pictureScanTrigger;
    }

    /**
     * 初始化图片任务和触发器
     *
     * @throws org.quartz.SchedulerException
     */
    private void initScheduler() throws SchedulerException {
        log.info("图片同步开启");

        //图片扫描任务
        JobDetail job = JobBuilder.newJob(PictureScanJob.class)
                .withIdentity(PICTURE_SCAN_JOB, PICTURE_SCAN_GROUP)
                .storeDurably().build();

        scheduler.addJob(job, false);

        DVPlaceService dvPlaceService = applicationContext.getBean(DVPlaceService.class);

        // 启用的摄像机数目
        long enableDevicesCount = dvPlaceService.findEnableCount();
        for (int i = 0; i * BATCH_SIZE < enableDevicesCount; i++) {
            addTriggers(dvPlaceService.findAllEnable(i * BATCH_SIZE, BATCH_SIZE));
        }

    }

    private void addTriggers(Collection<? extends DVPlaceBean> dvPlaces) {
        for (DVPlaceBean dvPlace : dvPlaces) {
            pictureScanTrigger.addTrigger(dvPlace);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        String errorMessage = "图片同步目录配置不正确: config.properties/proxima.images.dir.relative";
        this.applicationContext = applicationContext;

        String dirRelative = Configs.get("proxima.images.dir.relative");
        if (Strings.isNullOrEmpty(dirRelative)) {
            throw new IllegalArgumentException(errorMessage);
        }

        dirRelative = dirRelative.trim();
        int dirRelativeMode = -1;
        try {
            dirRelativeMode = Integer.parseInt(dirRelative);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }

        String imagesDir = Configs.get("proxima.images.dir");
        if (Strings.isNullOrEmpty(imagesDir)) {
            throw new IllegalArgumentException("图片同步目录配置不正确: config.properties/proxima.images.dir");
        }

        // 相对于项目路径
        if (dirRelativeMode == 1) {
            if (!(applicationContext instanceof WebApplicationContext)) {
                throw new IllegalArgumentException("获取不到项目路径");
            }

            ServletContext servletContext = ((WebApplicationContext) applicationContext).getServletContext();
            // 获取应用所在文件系统中的位置
            webAppBasePath = servletContext.getRealPath("/");
            imageResourcesRootDir = new File(webAppBasePath, imagesDir);
            imageResourcesRootDirPath = imageResourcesRootDir.getPath();
            if (!imageResourcesRootDir.exists()) {
                imageResourcesRootDir.mkdirs();
            }
        }
        // 绝对路径
        else if (dirRelativeMode == 0) {
            imageResourcesRootDirPath = imagesDir;
            imageResourcesRootDir = new File(imagesDir);
        } else {
            throw new IllegalArgumentException(errorMessage);
        }

    }

    public static String getImageResourcesRootDirPath() {
        return imageResourcesRootDirPath;
    }

    public static File getImageResourcesRootDir() {
        return imageResourcesRootDir;
    }
}
