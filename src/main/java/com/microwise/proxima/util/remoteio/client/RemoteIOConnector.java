package com.microwise.proxima.util.remoteio.client;

import com.microwise.proxima.util.remoteio.RemoteIO;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * 将 IO模块 工作模式为 TCP server 时，使用此类.
 * <p>
 * 现在使用的远程IO模块通常会有4路输出，每路输出都是一个继电器.每个继电器类似于单刀双掷开关。
 * 继电器从左往右，有三根线。左边两根或者右边两根总有一组默认是接通的。
 * </p>
 * 例子:
 * <p/>
 * <pre>
 * // 远程IO模块地址
 * InetSocketAddress remoteAddress = new InetSocketAddress(&quot;192.168.0.253&quot;, 10001);
 * // 链接远程IO模块
 * RemoteIOConnector remoteIOConnector = new RemoteIOConnector(remoteAddress);
 *
 * // 打开第一路左边
 * remoteIOConnector.turnOn(1);
 * // 关闭第一路左边
 * remoteIOConnector.turnOff(1);
 *
 * // 关键远程IO模块链接
 * remoteIOConnector.close();
 * </pre>
 * <p/>
 * User: gaohui Date: 12-7-25 Time: 下午2:17
 */
public class RemoteIOConnector extends RemoteIO {
    private IoConnector connector;

    /**
     * 如果远程IO模块使用 TCP Server 模式工作，只需要传 IO模块的 IP 和 端口
     *
     * @param socketAddress
     */
    public RemoteIOConnector(InetSocketAddress socketAddress) {
        connector = new NioSocketConnector();

        connector.setHandler(new IoHandlerAdapter() {
            @Override
            public void messageReceived(IoSession session, Object message)
                    throws Exception {
                lock.lock();
                try {
                    writeBackCondition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        });

        try {
            ConnectFuture connectFuture = connector.connect(socketAddress);
            connectFuture.awaitUninterruptibly();
            session = connectFuture.getSession();
        } catch (RuntimeException e) {
            connector.dispose();
            throw e;
        }

    }

    @Override
    public void close() {
        if (session != null && session.isConnected()) {
            session.close(true);
            connector.dispose();
        }
    }

//    /**
//     * 获取服务端地址
//     *
//     * @return
//     * @author zhang.licong
//     * @date 2012-8-15
//     */
//    public SocketAddress getRemoteAddress() {
//        return this.session.getRemoteAddress();
//    }
//
//    /**
//     * 将某路继电器左路接通
//     *
//     * @param route 第几路（从1开始）
//     */
//    public void switchToLeft(int route) {
//        Preconditions.checkArgument(route <= 4 && route > 0, "路数在 1 到 4 之间");
//
//        switchTo(switchToLeftCommand(route));
//    }
//
//    /**
//     * 将某路继电器右路接通
//     *
//     * @param route 第几路（从1开始）
//     */
//    public void switchToRight(int route) {
//        Preconditions.checkArgument(route <= 4 && route > 0, "路数在 1 到 4 之间");
//
//        switchTo(switchToRightCommand(route));
//    }
//
//    @Override
//    public void turnOn(int route) {
//        switchToLeft(route);
//    }
//
//    @Override
//    public void turnOff(int route) {
//        switchToRight(route);
//    }
//
//    /**
//     * 将哪一路打开
//     *
//     * @param switchCommand
//     */
//    private void switchTo(IoBuffer switchCommand) {
//        // 如果链接断开
//        if (!session.isConnected()) {
//            // TODO 使用更合适的异常
//            throw new IllegalStateException("链接已断开");
//        }
//
//        lock.lock();
//        try {
//
//            session.write(switchCommand).awaitUninterruptibly();
//
//            // 如果已经有回复，那么则不需要等待；如果没有回复，则等待回复
//            if (writeBackMessage == null) {
//                try {
//                    writeBackCondition.await(10, TimeUnit.SECONDS); // 最多等 10 秒
//                    logger.debug(writeBackMessage);
//                } catch (InterruptedException e) {
//                    // TODO 使用更合适的异常
//                    throw new IllegalStateException("未收到回复", e);
//                }
//            }
//        } finally {
//            // 清空回复
//            writeBackMessage = null;
//            lock.unlock();
//        }
//    }
//
//    /**
//     * 打开左边命令
//     *
//     * @param route
//     * @return
//     */
//    private static IoBuffer switchToLeftCommand(int route) {
////        byte[] openCommand = new byte[]{0x01, 0x05, 0x00, (byte) (route - 1),
////                (byte) 0xFF, 0x00};
//        byte[] openCommand = new byte[]{(byte) 0xEA, (byte) 0xA1, (byte) route, (byte) 0xEB};
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
////        byte[] closeCommand = new byte[]{0x01, 0x05, 0x00,
////                (byte) (route - 1), (byte) 0x00, 0x00};
//        byte[] closeCommand = new byte[]{(byte) 0xEA, (byte) 0xA2, (byte) route, (byte) 0xEB};
//        return IoBuffer.wrap(closeCommand);
//    }
//
//    /**
//     * 关闭链接
//     */
//    public void close() {
//        if (session != null && session.isConnected()) {
//            session.close(true);
//            connector.dispose();
//        }
//    }
//
//    @Override
//    public List<String> findState() {
//        switchTo(findRelaysState());
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
//        findStateMessage = null;
//        return onOrOffStr;
//    }
//
//    @Override
//    public Object findCurrentState() {
////        return switchTo(findRelaysState());
//        return null;
//    }
//
//    /**
//     * 获取io模块的链路状态
//     *
//     * @return
//     */
//    private static IoBuffer findRelaysState() {
////        byte[] findCommand = new byte[]{0x01, 0x02, 0x00, 0x00, 0x00, 0x00};
//
//        byte[] findCommand = new byte[]{(byte) 0xEA, (byte) 0xA5, (byte) 0xAA, (byte) 0xEB};
//        return IoBuffer.wrap(findCommand);
//    }
}
