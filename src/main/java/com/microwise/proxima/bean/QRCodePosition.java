package com.microwise.proxima.bean;

/**
 * 二维码位置
 * <p/>
 * 点之间有顺序
 *
 * @author gaohui
 * @date 13-3-19 14:21
 */
public class QRCodePosition {
    private Point point0;
    private Point point1;
    private Point point2;


    public Point getPoint0() {
        return point0;
    }

    public void setPoint0(Point point0) {
        this.point0 = point0;
    }

    public Point getPoint1() {
        return point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }
}
