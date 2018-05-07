package com.microwise.proxima.action;

import com.microwise.proxima.photograph.RemoteIOServerHolder;
import com.microwise.proxima.util.remoteio.RemoteIO;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 控制IO模块开关
 *
 * @author liuzhu
 * @date 14-1-26
 */
@Component()
@Scope("prototype")
public class ControlAction {
    public static final Logger log = LoggerFactory.getLogger(ControlAction.class);

    /**
     * IO模块版本
     */
    private int ioVersion;

    /**
     * 端口
     */
    private int port;

    /**
     * 路数
     */
    private int route;

    private Map session = ActionContext.getContext().getSession();

    public String turnOn() {
        try {
            RemoteIO remoteIO = RemoteIOServerHolder.getInstance().getRemoteIOServer().getRemoteIO(port);
            remoteIO.setIoVersion(ioVersion);
            remoteIO.turnOn(route);
            session.put(port, route);
        } catch (IOException e) {
            log.error("打开IO模块失败", e);
        }
        return Action.SUCCESS;
    }

    public String turnOff() {
        try {
            RemoteIO remoteIO = RemoteIOServerHolder.getInstance().getRemoteIOServer().getRemoteIO(port);
            remoteIO.setIoVersion(ioVersion);
            remoteIO.turnOff(route);
            session.put(port, route);
        } catch (IOException e) {
            log.error("关闭IO模块失败", e);
        }
        return Action.SUCCESS;
    }

    public int getIoVersion() {
        return ioVersion;
    }

    public void setIoVersion(int ioVersion) {
        this.ioVersion = ioVersion;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
