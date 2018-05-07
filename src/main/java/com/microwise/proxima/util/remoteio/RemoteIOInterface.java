package com.microwise.proxima.util.remoteio;

import java.net.SocketAddress;
import java.util.List;

/**
 * 此类代表一个 远程IO模块， 通过此类操作 远程IO模块
 * 此类只能通过 RemoteIOServer 获取.
 * <p>
 * 远程IO模块通常有几路开关，每路开关相当于一个继电器.每个继电器类似于单刀双掷开关。
 * 继电器从左往右，有三根线。左边两根或者右边两根总有一组默认是接通的。通常是左边两根断开，右边两根接通。
 * </p>
 * <p/>
 * Date: 12-8-9 Time: 下午5:58
 *
 * @author bastengao
 * @deprecated by li.jianfei 2016-09-06
 */
public interface RemoteIOInterface {

    /**
     * 获取远程IO模块远程地址(可能是IO模块的局域网内的内网地址，也可能是IO模块的外网地址)
     *
     * @return
     */
    public SocketAddress getRemoteAddress();

    /**
     * 将某路的左边接通(右边接通)
     *
     * @param route 第几路（从1开始）
     */
    void turnOn(int route);

    /**
     * 将某路的左边断开(右边接通)
     *
     * @param route 第几路（从1开始）
     */
    void turnOff(int route);

    /**
     * 关闭链接
     */
    void close();

    /**
     * 查询状态
     *
     * @return 路数开关集合
     * @author liuzhu
     * @date 2014-2-10
     */
    List<String> findState();

    /**
     * 查询当前状态
     *
     * @author liuzhu
     * @date 2014-5-30
     */
    Object findCurrentState();
}
