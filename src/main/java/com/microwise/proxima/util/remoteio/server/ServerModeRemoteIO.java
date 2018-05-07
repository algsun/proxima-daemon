package com.microwise.proxima.util.remoteio.server;

import com.microwise.proxima.util.remoteio.RemoteIO;
import org.apache.mina.core.session.IoSession;

/**
 * 此类代表一个 远程IO模块， 通过此类操作 远程IO模块
 * 此类只能通过 RemoteIOServer 获取.
 * <p>
 * 远程IO模块通常有几路开关，每路开关相当于一个继电器.每个继电器类似于单刀双掷开关。
 * 继电器从左往右，有三根线。左边两根或者右边两根总有一组默认是接通的。通常是左边两根断开，右边两根接通。
 * </p>
 * Date: 12-8-9 Time: 下午3:34
 *
 * @author bastengao
 */
public class ServerModeRemoteIO extends RemoteIO {

    /**
     *
     */
    ServerModeRemoteIO(IoSession session) {
        this.session = session;
    }

    /**
     * 当有消息回复时
     *
     * @param writeBackMessage
     */
    public void writeBack(Object writeBackMessage) {
        lock.lock();
        try {
            super.writeBackMessage = writeBackMessage;
            writeBackCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

//    /**
//     * 获取客户端地址
//     *
//     * @author zhang.licong
//     * @date 2012-8-15
//     */
//    public SocketAddress getRemoteAddress() {
//        return this.session.getRemoteAddress();
//    }
//
//
//    /**
//     * 将某路的左边接通(右边断开)
//     *
//     * @param route 第几路（从1开始）
//     */
//    @Override
//    public void turnOn(int route) {
//        switchToLeft(route);
//    }
//
//    /**
//     * 将某路的左边断开(右边接通)
//     *
//     * @param route 第几路（从1开始）
//     */
//    @Override
//    public void turnOff(int route) {
//        switchToRight(route);
//    }
//
//
//    @Override
//    public void close() {
//        if (session != null || session.isConnected()) {
//            session.close(true);
//        }
//    }
//
//
//
//    // TODO return List<Boolean> @gaohui 2014-04-02
//    @Override
//    public List<String> findState() {
//        // TODO 返回状态即可，不要和UI耦合 @gaohui 2014-04-02
//
//        //查询状态消息
//        Object findStateMessage = sendCommand(findRelaysState());
//
//        if(findStateMessage == null){
//            return Lists.newArrayList("无结果", "无结果", "无结果", "无结果");
//        }
//
//        List<String> onOrOffStr = new ArrayList<String>();
//        if (findStateMessage instanceof IoBuffer) {
//            IoBuffer buffer = (IoBuffer) findStateMessage;
//            byte b = buffer.get(5);
//            for (int i = 1; i <= 4; i++) {
//                int onOrOff = (b >> (i - 1)) & 0x0001;
//                if (onOrOff == 1) {
//                    onOrOffStr.add(i + "路:" + "<b>关</b>");
//                } else {
//                    onOrOffStr.add(i + "路:" + "<b>开</b>");
//                }
//            }
//        }
//        return onOrOffStr;
//    }
//
//    @Override
//    public Object findCurrentState() {
//        //查询状态消息
//         return sendCommand(findRelaysState());
//    }
//
//    /**
//     * 将某路继电器左路接通
//     *
//     * @param route 第几路（从1开始）
//     */
//    private void switchToLeft(int route) {
//        Preconditions.checkArgument(route <= MAX_ROUTE && route > 0, "路数在 1 到 4 之间");
//
//        sendCommand(switchToLeftCommand(route));
//    }
//
//    /**
//     * 将某路继电器右路接通
//     *
//     * @param route 第几路（从1开始）
//     */
//    private void switchToRight(int route) {
//        Preconditions.checkArgument(route <= MAX_ROUTE && route > 0, "路数在 1 到 4 之间");
//
//        sendCommand(switchToRightCommand(route));
//    }
//
//    /**
//     * 将哪一路打开
//     *
//     * @param command
//     */
//    private Object sendCommand(IoBuffer command) {
//        //如果链接断开
//        if (!session.isConnected()) {
//            //TODO 使用更合适的异常
//            throw new IllegalStateException("链接已断开");
//        }
//
//        lock.lock();
//        try {
//
//            session.write(command).awaitUninterruptibly();
//            logger.debug("开始发送命令");
//
//            //如果已经有回复，那么则不需要等待；如果没有回复，则等待回复
//            if (writeBackMessage == null) {
//                try {
//                    writeBackCondition.await(10, TimeUnit.SECONDS); //最多等 10 秒
//                    logger.debug("结束返回信息：" + writeBackMessage);
//                } catch (InterruptedException e) {
//                    //TODO 使用更合适的异常
//                    throw new IllegalStateException("未收到回复", e);
//                }
//            }
//
//            return writeBackMessage;
//        } finally {
//            //清空回复
//            writeBackMessage = null;
//            lock.unlock();
//        }
//    }
//
//
//    /**
//     * 打开左边命令
//     *
//     * @param route
//     * @return
//     */
//    private static IoBuffer switchToLeftCommand(int route) {
//        byte[] openCommand = new byte[]{0x01, 0x05, 0x00, (byte) (route - 1), (byte) 0xFF, 0x00};
//        return IoBuffer.wrap(openCommand);
//    }
//
//    /**
//     * 打开右边命令
//     *
//     * @param route
//     * @return
//     */
//    private static IoBuffer switchToRightCommand(int route) {
//        byte[] closeCommand = new byte[]{0x01, 0x05, 0x00, (byte) (route - 1), (byte) 0x00, 0x00};
//        return IoBuffer.wrap(closeCommand);
//    }
//
//    /**
//     * 获取io模块的链路状态
//     *
//     * @return
//     */
//    private static IoBuffer findRelaysState() {
//        byte[] findCommand = new byte[]{0x01, 0x02, 0x00, 0x00, 0x00, 0x00};
//        return IoBuffer.wrap(findCommand);
//    }


}
