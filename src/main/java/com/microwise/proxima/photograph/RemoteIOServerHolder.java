package com.microwise.proxima.photograph;

import com.microwise.proxima.util.remoteio.server.RemoteIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 单例，保持 RemoteIOServer 只有一个. 整个应用中从此类获取 RemoteIOServer.
 *
 * @author zhang.licong
 * @date 2012-8-11
 */
public class RemoteIOServerHolder {
    private static final Logger log = LoggerFactory.getLogger(RemoteIOServerHolder.class);

    private static RemoteIOServerHolder instance = new RemoteIOServerHolder();
    private RemoteIOServer remoteIOServer = null;
    private int port;

    private RemoteIOServerHolder() {
    }

    public static RemoteIOServerHolder getInstance() {
        return instance;
    }

    public synchronized void initPort(int port) {
        log.info("初始化 IO 模块端口");
        this.port = port;
    }

    public synchronized RemoteIOServer getRemoteIOServer() throws IOException {
        if (remoteIOServer == null) {
            log.info("初始化 IO 模块 Server");
            remoteIOServer = new RemoteIOServer(port);
        }
        return remoteIOServer;
    }

}
