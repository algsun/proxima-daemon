package com.microwise.proxima.app;

import com.microwise.proxima.photograph.RemoteIOServerHolder;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

/**
 * 资源释放
 *
 * @author zhang.licong
 * @date 2012-8-11
 */
public class ReleaseResourceListener implements ServletContextListener {
    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("release resources");

        //关闭 RemoteIOServer, 如果存在
        try {
            if (RemoteIOServerHolder.getInstance().getRemoteIOServer() != null) {
                log.info("close RemoteIOServer");
                RemoteIOServerHolder.getInstance().getRemoteIOServer().close();
            }
        } catch (IOException e) {
            log.error("关闭 RemoteIOServer 错误", e);
        }

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

}
