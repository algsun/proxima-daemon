package com.microwise.proxima.util.remoteio;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
 */
public abstract class RemoteIO {
    private final Logger logger = Logger.getLogger(this.getClass());
    private static final int MAX_ROUTE = 4;
    public static final int IO_VERSION_1 = 1;
    public static final int IO_VERSION_2 = 2;
    protected IoSession session;
    protected Lock lock = new ReentrantLock();
    protected Condition writeBackCondition = lock.newCondition();

    protected Object writeBackMessage = null;

    private int ioVersion;

    /**
     * 获取远程IO模块远程地址(可能是IO模块的局域网内的内网地址，也可能是IO模块的外网地址)
     *
     * @return
     */
    public SocketAddress getRemoteAddress() {
        return this.session.getRemoteAddress();
    }

    /**
     * 将某路的左边接通(右边接通)
     *
     * @param route 第几路（从1开始）
     */
    public void turnOn(int route) {
        switchToLeft(route);
    }


    /**
     * 将某路的左边断开(右边接通)
     *
     * @param route 第几路（从1开始）
     */
    public void turnOff(int route) {
        switchToRight(route);
    }

    /**
     * 关闭链接
     */
    public void close() {
        if (session != null && session.isConnected()) {
            session.close(true);
        }
    }

    /**
     * 查询状态
     *
     * @return 路数开关集合
     * @author liuzhu
     * @date 2014-2-10
     */
    // TODO return List<Boolean> @gaohui 2014-04-02
    public List<String> findState() {
        // TODO 返回状态即可，不要和UI耦合 @gaohui 2014-04-02

        //查询状态消息
        Object findStateMessage = sendCommand(findRelaysState());

        if (findStateMessage == null) {
            return Lists.newArrayList("无结果", "无结果", "无结果", "无结果");
        }

        List<String> onOrOffStr = new ArrayList<String>();
        if (findStateMessage instanceof IoBuffer) {
            IoBuffer buffer = (IoBuffer) findStateMessage;
            byte b = (byte) 0xff;
            if (getIoVersion() == IO_VERSION_1) {
                b = buffer.get(5);
            } else if (getIoVersion() == IO_VERSION_2) {
                b = buffer.get(3);
            }
            for (int i = 1; i <= 4; i++) {
                int onOrOff = (b >> (i - 1)) & 0x0001;
                if (onOrOff == 1) {
                    onOrOffStr.add(i + "路:" + "<b>关</b>");
                } else {
                    onOrOffStr.add(i + "路:" + "<b>开</b>");
                }
            }
        }
        return onOrOffStr;
    }

    /**
     * 查询当前状态
     *
     * @author liuzhu
     * @date 2014-5-30
     */
    public Object findCurrentState() {
        return sendCommand(findRelaysState());
    }

    /**
     * 将某路继电器左路接通
     *
     * @param route 第几路（从1开始）
     */
    public void switchToLeft(int route) {
        Preconditions.checkArgument(route <= MAX_ROUTE && route > 0, "路数在 1 到 4 之间");

        sendCommand(switchToLeftCommand(route));
    }

    /**
     * 将某路继电器右路接通
     *
     * @param route 第几路（从1开始）
     */
    public void switchToRight(int route) {
        Preconditions.checkArgument(route <= MAX_ROUTE && route > 0, "路数在 1 到 4 之间");

        sendCommand(switchToRightCommand(route));
    }

    /**
     * 将哪一路打开
     *
     * @param command
     */
    private Object sendCommand(IoBuffer command) {
        //如果链接断开
        if (!session.isConnected()) {
            //TODO 使用更合适的异常
            throw new IllegalStateException("链接已断开");
        }

        lock.lock();
        try {

            session.write(command).awaitUninterruptibly();
            logger.debug("开始发送命令");
            //如果已经有回复，那么则不需要等待；如果没有回复，则等待回复
            if (writeBackMessage == null) {
                try {
                    writeBackCondition.await(10, TimeUnit.SECONDS); //最多等 10 秒
                    logger.debug("结束返回信息：" + writeBackMessage);
                } catch (InterruptedException e) {
                    //TODO 使用更合适的异常
                    writeBackMessage = null;
                    throw new IllegalStateException("未收到回复", e);
                }
            }
            return writeBackMessage;
        } finally {
            //清空回复
            writeBackMessage = null;
            lock.unlock();
        }
    }


    /**
     * 打开左边命令
     *
     * @param route
     * @return
     */
    private IoBuffer switchToLeftCommand(int route) {
        byte[] openCommand = new byte[]{};
        if (getIoVersion() == IO_VERSION_1) {
            openCommand = new byte[]{0x01, 0x05, 0x00, (byte) (route - 1), (byte) 0xFF, 0x00};
        } else if (getIoVersion() == IO_VERSION_2) {
            openCommand = new byte[]{(byte) 0xEA, (byte) 0xA1, (byte) route, (byte) 0xEB};
        }
        return IoBuffer.wrap(openCommand);
    }

    /**
     * 打开右边命令
     *
     * @param route
     * @return
     */
    private IoBuffer switchToRightCommand(int route) {
        byte[] closeCommand = new byte[]{};
        if (getIoVersion() == IO_VERSION_1) {
            closeCommand = new byte[]{0x01, 0x05, 0x00, (byte) (route - 1), (byte) 0x00, 0x00};
        } else if (getIoVersion() == IO_VERSION_2) {
            closeCommand = new byte[]{(byte) 0xEA, (byte) 0xA2, (byte) route, (byte) 0xEB};
        }
        return IoBuffer.wrap(closeCommand);
    }

    /**
     * 获取io模块的链路状态
     *
     * @return
     */
    private IoBuffer findRelaysState() {
        byte[] findCommand = new byte[]{(byte) 0xEA, (byte) 0xA5, (byte) 0xAA, (byte) 0xEB};
        if (getIoVersion() == IO_VERSION_1) {


            findCommand = new byte[]{0x01, 0x02, 0x00, 0x00, 0x00, 0x00};
        } else if (getIoVersion() == IO_VERSION_2) {
            findCommand = new byte[]{(byte) 0xEA, (byte) 0xA5, (byte) 0xAA, (byte) 0xEB};
        }
        return IoBuffer.wrap(findCommand);
    }


    public int getIoVersion() {
        return ioVersion;
    }

    public void setIoVersion(int ioVersion) {
        this.ioVersion = ioVersion;
    }
}
