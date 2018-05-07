package com.microwise.proxima.beans2;

import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.bean.Point;

/**
 * 表示一个纯粹的二维码. 注意：二维码的三个点是有顺序的
 *
 * 只是为了调试, 看一个图片有哪些二维码被识别了
 *
 * Date: 12-9-27 Time: 下午5:59
 *
 * @author bastengao
 */
public class IdentityCode {
    private int id;

    private String text;

    /**
     * 第一个点
     */
    private Point firstPoint;

    /**
     * 第二个点
     */
    private Point secondPoint;

    /**
     * 第三个点
     */
    private Point thirdPoint;

    private PictureBean picture;

    public Point getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(Point firstPoint) {
        this.firstPoint = firstPoint;
    }

    public Point getSecondPoint() {
        return secondPoint;
    }

    public void setSecondPoint(Point secondPoint) {
        this.secondPoint = secondPoint;
    }

    public Point getThirdPoint() {
        return thirdPoint;
    }

    public void setThirdPoint(Point thirdPoint) {
        this.thirdPoint = thirdPoint;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PictureBean getPicture() {
        return picture;
    }

    public void setPicture(PictureBean picture) {
        this.picture = picture;
    }
}
