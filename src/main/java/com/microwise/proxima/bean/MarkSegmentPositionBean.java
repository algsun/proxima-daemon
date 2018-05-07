package com.microwise.proxima.bean;

import java.util.Date;


/**
 * 标记段坐标
 *
 * @author gaohui
 * @date 2012-7-12
 */
public class MarkSegmentPositionBean {
    /**
     * ID
     */
    private String id;

    /**
     * 标记段
     */
    private MarkSegmentBean markSegment;

    /**
     * 图片
     */
    private PictureBean picture;

    /**
     * 图片上传时间
     */
    private Date picSaveTime;

    /**
     * 第一个点坐标X
     */
    private int positionX;

    /**
     * 第一个点坐标Y
     */
    private int positionY;

    /**
     * 第二点坐标X
     */
    private int positionX2;

    /**
     * 第二个点坐标Y
     */
    private int positionY2;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 两点长度，单位 毫米(mm)
     *
     * TODO 重命名为 length @gaohui 2013-03-19
     *
     * TODO 可以考虑将单位改为像素 @gaohui 2013-03-19
     */
    private float markLength;

    /**
     * 此次两点长度与上一次编辑两点长度的差值(绝对值)，单位毫米(mm)
     */
    private float lengthDelta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MarkSegmentBean getMarkSegment() {
        return markSegment;
    }

    public void setMarkSegment(MarkSegmentBean markSegment) {
        this.markSegment = markSegment;
    }

    public PictureBean getPicture() {
        return picture;
    }

    public void setPicture(PictureBean picture) {
        this.picture = picture;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionX2() {
        return positionX2;
    }

    public void setPositionX2(int positionX2) {
        this.positionX2 = positionX2;
    }

    public int getPositionY2() {
        return positionY2;
    }

    public void setPositionY2(int positionY2) {
        this.positionY2 = positionY2;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public float getMarkLength() {
        return markLength;
    }

    public void setMarkLength(float markLength) {
        this.markLength = markLength;
    }

    public float getLengthDelta() {
        return lengthDelta;
    }

    public void setLengthDelta(float lengthDelta) {
        this.lengthDelta = lengthDelta;
    }

    public Date getPicSaveTime() {
        return picSaveTime;
    }

    public void setPicSaveTime(Date picSaveTime) {
        this.picSaveTime = picSaveTime;
    }
}
