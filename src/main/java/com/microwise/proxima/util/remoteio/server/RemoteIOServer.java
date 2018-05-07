package com.microwise.proxima.util.remoteio.server;

import com.microwise.proxima.util.remoteio.RemoteIO;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 当 IO 模块工作为 client 端时，需要控制端作服务器, 此主要来完成服务器工作。
 * Date: 12-8-9 Time: 下午3:04
 *
 * @author bastengao
 */
@Component
public class RemoteIOServer implements Closeable, ApplicationContextAware {

    private static ApplicationContext applicationContext; // Spring应用上下文环境


    //set注入Session，struts2把session封装成了一个Map

    public static RemoteIOHandler remoteIOHandler;
    private final IoAcceptor acceptor = new NioSocketAcceptor();;

    private RemoteIOServer() {
    }

    public RemoteIOServer(int port) throws IOException {

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

        LoggingFilter loggingFilter = new LoggingFilter();
        chain.addLast("logging", loggingFilter);
        remoteIOHandler = new RemoteIOHandler();
        acceptor.setHandler(remoteIOHandler);
        acceptor.bind(new InetSocketAddress(port));
        ((XmlWebApplicationContext) applicationContext).getServletContext().setAttribute("acceptor", acceptor);
    }

    /**
     * 是否有某个端口的远程IO模块
     *
     * @param port
     * @return
     */
    public boolean hasRemoteIO(int port) {
        return remoteIOHandler.contains(port);
    }

    /**
     * 根据远程IO模块的端口查找 远程IO模块
     *
     * @param port
     * @return
     */
    public RemoteIO getRemoteIO(int port) {
        return remoteIOHandler.get(port);
    }

    @Override
    public void close() throws IOException {
        acceptor.dispose(true);
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
