package com.microwise.proxima.util.remoteio.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Date: 12-8-9 Time: 下午3:08
 *
 * @author bastengao
 */
public class RemoteIOHandler extends IoHandlerAdapter {
    private final static Logger log = LoggerFactory.getLogger(RemoteIOHandler.class);

    //一个 "socket 地址"(IP + port )对应一个 ServerModeRemoteIO
    private ConcurrentMap<InetSocketAddress, ServerModeRemoteIO> remoteIOs = new ConcurrentHashMap<InetSocketAddress, ServerModeRemoteIO>();

    /**
     * 返回 IP 和 端口 匹配的 ServerModeRemoteIO, 如果没有返回 null
     *
     * @param host
     * @param port
     * @return
     */
    public ServerModeRemoteIO get(String host, int port) {
        return remoteIOs.get(new InetSocketAddress(host, port));
    }

    /**
     * 返回端口匹配的第一个 ServerModeRemoteIO, 如果没有返回 nul
     *
     * @param port
     * @return
     */
    public ServerModeRemoteIO get(int port) {
        Set<InetSocketAddress> keys = remoteIOs.keySet();
        for (InetSocketAddress inetSocketAddress : keys) {
            if (inetSocketAddress.getPort() == port) {
                return remoteIOs.get(inetSocketAddress);
            }
        }
        return null;
    }

    public boolean contains(int port) {
        Set<InetSocketAddress> keys = remoteIOs.keySet();
        for (InetSocketAddress inetSocketAddress : keys) {
            if (inetSocketAddress.getPort() == port) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.debug("connected : {}", session.getRemoteAddress());
        ServerModeRemoteIO remoteIO = new ServerModeRemoteIO(session);
        remoteIOs.putIfAbsent((InetSocketAddress) session.getRemoteAddress(), remoteIO);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.debug("received : {}", message);
        if (remoteIOs.containsKey(session.getRemoteAddress())) {
            ServerModeRemoteIO remoteIOHandler = remoteIOs.get(session.getRemoteAddress());
            remoteIOHandler.writeBack(message);
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
    	log.debug("移出会话:{}",session);
        remoteIOs.remove(session.getRemoteAddress());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    	log.debug("异常",cause);
    }

    public ConcurrentMap<InetSocketAddress, ServerModeRemoteIO> getRemoteIOs() {
        return remoteIOs;
    }
}
