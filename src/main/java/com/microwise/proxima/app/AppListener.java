package com.microwise.proxima.app;

import com.jcabi.manifests.Manifests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Date;

/**
 * @author gaohui
 * @date 14-4-2 10:02
 */
public class AppListener implements ServletContextListener {
    public final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 启动时间
        sce.getServletContext().setAttribute("app.startTime", new Date());

        try {
            Manifests.append(sce.getServletContext());
        } catch (Exception e) {
            log.error("获取项目启动时间", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
