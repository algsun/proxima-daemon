package com.microwise.proxima.imagesync.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 将多个任务封装成一个任务顺序执行
 *
 * @author gaohui
 * @date 13-6-19 17:28
 */
public class SerializeMultiTask implements Runnable {
    public static final Logger log = LoggerFactory.getLogger(SerializeMultiTask.class);
    private List<Runnable> tasks;

    public SerializeMultiTask(List<Runnable> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        for (Runnable task : tasks) {
            try {
                task.run();
            } catch (Throwable e) {
                log.error("", e);
            }
        }
    }
}
