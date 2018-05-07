package com.microwise.proxima.imagesync;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.FTPProfile;
import com.microwise.proxima.exception.FtpFailure;
import com.microwise.proxima.util.QuartzUtil;
import it.sauronsoftware.ftp4j.FTPClient;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * 图片扫描任务计划程序
 * <p/>
 * <p>
 * 注意：每个摄像机点位有且只有一个图片同步任务, 图片同步任务周期执行。
 * 如果不控制，可能会出现下面的情况，上一次的图片同步任务还没有执行完毕, 新的图片同步任务又开始执行。
 * 就会出现一个摄像机下有两个并发执行的图片同上任务，这样的情况是我们不想看见（两上任务会进行资源竞争, 结果无法预期）。
 * 所以我们理想的情况是一个摄像机点位最多只有一个图片同上任务， 可以限制任务并发执行 @gaohui 2013-03-27
 * </p>
 *
 * @author zhang.licong
 * @author gaohui
 * @date 2012-7-6
 * @date 2013-03-27
 * @check @xubaoji #2377 2013-03-29
 * @author song.tao 2014-6-23
 */
public class PictureScanJob implements Job {
    public static final Logger log = LoggerFactory.getLogger(PictureScanJob.class);

    private PictureScan pictureScan;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 如果存在还在运行 job, 直接返回
        try {
            if (QuartzUtil.existsRunningJobOfSameTrigger(jobExecutionContext, this)) {
                log.debug("正在运行，直接返回");
                return;
            }
        } catch (SchedulerException e) {
            throw new JobExecutionException(e);
        }

        JobDataMap jobDataMap = jobExecutionContext.getTrigger().getJobDataMap();

        ApplicationContext applicationContext = (ApplicationContext) jobDataMap.get(PictureScanTrigger.JOB_DATA_APP_CONTEXT);
        DVPlaceBean dvPlaceBean = (DVPlaceBean) jobDataMap.get(PictureScanTrigger.JOB_DATA_DV_PLACE);
        pictureScan = applicationContext.getBean(PictureScan.class);

        log.debug("扫描图片开始 CODE: " + dvPlaceBean.getPlaceCode());
        run(dvPlaceBean);
        log.debug("扫描图片结束 CODE: " + dvPlaceBean.getPlaceCode());
    }

    /**
     * 主执行方法
     *
     * @author zhang.licong
     * @date 2012-7-12
     */
    public void run(DVPlaceBean dvPlaceBean) {
        log.debug("登陆FTP");
        FTPProfile ftpProfile = dvPlaceBean.getFtp();
        FTPClient client = null;
        try {
            client = FTPUtils.ftpLogin(ftpProfile);
            pictureScan.readyPictureProcess(client, dvPlaceBean);// 执行图片扫描
        } catch (FtpFailure e) {
            log.error("Ftp操作异常", e);
        } finally {
            FTPUtils.ftpLogout(client);
        }
    }
}
