package com.microwise.proxima.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author gaohui
 * @date 13-3-28 10:45
 */
public class QuartzUtil {

    /**
     * 相同的 Trigger 是否存在正在运行的 job  除了自己
     *
     * @param jobExecutionContext
     * @param currentJob
     * @return
     * @throws SchedulerException
     */
    public static boolean existsRunningJobOfSameTrigger(JobExecutionContext jobExecutionContext, Job currentJob) throws SchedulerException {
        List<JobExecutionContext> jobs = jobExecutionContext.getScheduler().getCurrentlyExecutingJobs();
        for (JobExecutionContext job : jobs) {
            // 相同的 Trigger ，且不是自己
            if (job.getTrigger().equals(jobExecutionContext.getTrigger()) && !job.getJobInstance().equals(currentJob)) {
                return true;
            }

        }
        return false;
    }
}

