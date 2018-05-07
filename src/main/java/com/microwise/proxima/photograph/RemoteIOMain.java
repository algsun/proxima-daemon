package com.microwise.proxima.photograph;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.microwise.proxima.bean.OpticsDVPlaceBean;
import com.microwise.proxima.util.Configs;
import com.microwise.proxima.util.remoteio.RemoteIO;
import com.microwise.proxima.util.remoteio.client.RemoteIOConnector;
import com.microwise.proxima.util.remoteio.server.RemoteIOServer;
import org.apache.mina.core.RuntimeIoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 远程IO模块主类
 *
 * @author zhang.licong
 * @date 2012-8-10
 * @check @gaohui #2207 2013-03-29
 */
@Component
@Scope("singleton")
public class RemoteIOMain {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public static final int SERVER_MODE_INT = 1; // 服务端模式
    public static final int CLIENT_MODE_INT = 0; // 客户端模式

    /**
     * port(IO模块端口) => 锁
     */
    private ConcurrentMap<Integer, Lock> ioLocks = new ConcurrentHashMap<Integer, Lock>();


    /**
     * 模式，标识本地是服务端还是客户端，0、客户端 1、服务端
     */
    private int mode = CLIENT_MODE_INT;

    public RemoteIOMain() {
        // 获取本地是客户端还是服务端，0、客户端 1、服务端
        this.mode = Integer.parseInt(Configs.get("remoteIO.mode"));
    }

    /**
     * IO模块控制总执行程序
     *
     * @author zhang.licong
     * @date 2012-8-11
     */
    public void executeMain(OpticsDVPlaceBean dvPlace, Date deadline) {
        // 为每个io 模块添加 锁
        int ioPort = dvPlace.getIoPort();
        Lock lock = ioLocks.putIfAbsent(ioPort, new ReentrantLock());
        if (lock == null) {
            lock = ioLocks.get(ioPort);
        }

        lock.lock();
        try {
            if (mode == CLIENT_MODE_INT) {
                clientIO(dvPlace, deadline);

            } else if (mode == SERVER_MODE_INT) {
                serverIO(dvPlace, deadline);

            } else {
                throw new IllegalArgumentException(String.format(
                        "mode:{%s} 类型不匹配", mode));
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 本地客户端IO控制处理
     *
     * @throws InterruptedException
     * @author zhang.licong
     * @date 2012-8-11
     */
    public void clientIO(OpticsDVPlaceBean dvPlace, Date deadline) throws InterruptedException {
        String ioIp = dvPlace.getIoIp();
        int ioPort = dvPlace.getIoPort();
        RemoteIO remoteIo = null;
        if (deadline == null) {
            //如果失败，最多连接三次
            remoteIo = doConnect(ioIp, ioPort, 3);
        } else {
            remoteIo = repeatedlyConnect(ioIp, ioPort, deadline);
        }


        if (remoteIo != null) {
            ioControl(remoteIo, dvPlace);
        } else {
            log.debug("未能与IO模块建立连接[{}:{}]", ioIp, ioPort);
        }
    }

    /**
     * 客户端连接IO模块，在连接失败的情况下进行重复连接，在间隔时间的2分之一时间内进行连接，
     *
     * @param ioIp     IO模块IP
     * @param ioPort   IO模块端口
     * @param deadline 最后尝试时间（单位：分钟）
     * @author zhang.licong
     * @date 2012-11-5
     */
    public RemoteIO repeatedlyConnect(String ioIp, int ioPort, Date deadline) {

        while (deadline.compareTo(new Date()) >= 1) {
            RemoteIO remoteIo = doConnect(ioIp, ioPort);
            if (remoteIo != null) {
                log.debug("远程IO控制模块({}:{}), 进行连接成功", ioIp, ioPort);
                return remoteIo;
            } else {
                log.debug("远程IO控制模块({}:{}), 进行连接失败", ioIp, ioPort);
            }
        }

        return null;

    }

    /**
     * 客户端连接IO模块，在连接失败的情况下，会多次连接，连接次数不会超过 maxTimes.
     * 注意：连接失败返回 null
     *
     * @param ioIp
     * @param ioPort
     * @param maxTimes
     * @return
     */
    private RemoteIO doConnect(String ioIp, int ioPort, int maxTimes) {
        for (int i = 0; i < maxTimes; i++) {
            RemoteIO remoteIo = doConnect(ioIp, ioPort);
            if (remoteIo != null) {
                log.debug(String.format("远程IO控制模块(%s:%s), 进行第%s次连接成功", ioIp,
                        ioPort, i));
                return remoteIo;
            } else {
                log.debug(String.format("远程IO控制模块(%s:%s), 进行第%s次连接失败", ioIp,
                        ioPort, i));
            }
        }
        return null;
    }

    /**
     * 创建 RemoteIO 对象, 如果连接失败返回 null.
     *
     * @param ioIp
     * @param ioPort
     * @return
     */
    private RemoteIO doConnect(String ioIp, int ioPort) {
        InetSocketAddress socketAddress = new InetSocketAddress(ioIp, ioPort);
        RemoteIO remoteIo;
        try {
            remoteIo = new RemoteIOConnector(socketAddress);
        } catch (RuntimeIoException e) {
            log.error(String.format("远程IO控制模块(%s:%s)连接失败.", ioIp, ioPort), e);
            return null;
        }
        return remoteIo;
    }

    /**
     * 本地服务端IO控制处理
     *
     * @throws java.io.IOException
     * @throws InterruptedException
     * @author zhang.licong
     * @date 2012-8-11
     */
    public void serverIO(OpticsDVPlaceBean dvPlace, Date deadline) throws IOException,
            InterruptedException {
        final RemoteIOServer remoteIOServer = RemoteIOServerHolder
                .getInstance().getRemoteIOServer();

        RemoteIO remoteIo;

        // 获取IO模块的端口
        final int ioPort = dvPlace.getIoPort();

        if (deadline == null) {
            remoteIo = serverDoConnect(ioPort, remoteIOServer);
        } else {
            remoteIo = repeatedlyServerConnect(ioPort, remoteIOServer, deadline);
        }

        /*******************
         * 此代码适用于大遗址 //获取客户端地址 //InetSocketAddress sk =
         * (InetSocketAddress)remoteIo.getRemoteAddress(); //更新到数据库 //String
         * host = sk.getHostName(); //dvPlaceService.updateDvIP(dvPlace.getId(),
         * host);
         ********************/
        if (remoteIo != null) {
            ioControl(remoteIo, dvPlace);
        } else {
            log.debug("[*:{}]未能与IO模块建立连接", ioPort);
        }

    }

    /**
     * 服务端连接IO模块
     *
     * @param ioPort         IO模块端口
     * @param remoteIOServer
     * @author zhang.licong
     * @date 2012-11-5
     */
    private RemoteIO serverDoConnect(final int ioPort, final RemoteIOServer remoteIOServer) {
        // 获取客户端连接超时判断
        TimeLimiter timeLimiter = new SimpleTimeLimiter();
        try {
            return timeLimiter.callWithTimeout(new Callable<RemoteIO>() {
                public RemoteIO call() {
                    RemoteIO localRemote;
                    while (true) {
                        if (remoteIOServer.hasRemoteIO(ioPort)) {
                            localRemote = remoteIOServer.getRemoteIO(ioPort);
                            break;
                        }
                    }
                    return localRemote;
                }
            }, 10, TimeUnit.SECONDS, false);
        } catch (Exception e) {
            log.error("获取连接失败", e);
            // 如果异常直接返回
            return null;
        }
    }

    /**
     * 服务端连接IO模块，在连接失败的情况下进行重复连接，在某个时间点之前一直进行连接，
     *
     * @param ioPort         IO模块端口
     * @param remoteIOServer
     * @param deadline       执行间隔事件
     * @author zhang.licong
     * @date 2012-11-5
     */
    private RemoteIO repeatedlyServerConnect(final int ioPort, final RemoteIOServer remoteIOServer, Date deadline) {
        log.debug("[*:{}]尝试获取链接", ioPort);

        // 如果存在连接，可能这个连接是假连接(死了，但还存在). 所以我们这里一棒子打死，统一关闭重连
        if (remoteIOServer.hasRemoteIO(ioPort)) {
            log.debug("[*:{}]连接存在,强制关闭已存在连接. 准备重连", ioPort);
            RemoteIO remoteIO = remoteIOServer.getRemoteIO(ioPort);
            if (remoteIO != null) {
                remoteIO.close();
            }
        }

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
        }

        //重新获取连接
        while (deadline.compareTo(new Date()) >= 1) {

            if (remoteIOServer.hasRemoteIO(ioPort)) {
                log.debug("[*:{}]获取链接成功", ioPort);
                return remoteIOServer.getRemoteIO(ioPort);
            }
        }
        log.debug("[*:{}]获取链接失败", ioPort);
        return null;
    }


    /**
     * IO模块控制
     *
     * @author zhang.licong
     * @date 2012-8-13
     */
    public void ioControl(RemoteIO remoteIo, OpticsDVPlaceBean dvPlace)
            throws InterruptedException {
        try {
            int ioDvRoute = dvPlace.getIoDvRoute();
            int ioLightRoute = dvPlace.getIoLightRoute();
            // 设置IO模块版本
            remoteIo.setIoVersion(dvPlace.getIoVersion());
            if (dvPlace.isLightOn()) {
                // 开灯
                log.debug("[{}:{}]准备开灯", dvPlace.getIoIp(), dvPlace.getIoPort());
                remoteIo.turnOn(ioLightRoute);
                TimeUnit.SECONDS.sleep(dvPlace.getLightOnTime());
            }

            // 拍照
            log.debug("[{}:{}]拍照开始", dvPlace.getIoIp(), dvPlace.getIoPort());
            remoteIo.turnOn(ioDvRoute);
            TimeUnit.MILLISECONDS.sleep(dvPlace.getPhotographTime());

            // 拍照结束
            log.debug("[{}:{}]拍照结束", dvPlace.getIoIp(), dvPlace.getIoPort());
            remoteIo.turnOff(ioDvRoute);
            TimeUnit.SECONDS.sleep(dvPlace.getLightOffTime());

            if (dvPlace.isLightOn()) {
                // 关灯
                log.debug("[{}:{}]准备关灯", dvPlace.getIoIp(), dvPlace.getIoPort());
                remoteIo.turnOff(ioLightRoute);
            }

        } finally {
            // 关闭连接
            log.debug("[{}:{}]关闭连接", dvPlace.getIoIp(), dvPlace.getIoPort());
            remoteIo.close();
        }
    }
}
