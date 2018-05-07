package com.microwise.proxima.bean;

import java.util.Date;

/**
 * 摄像机点位
 *
 * @author gaohui
 * @date 2012-7-6
 */
public class DVPlaceBean {

    private String id;      //id
    private MonitorPointBean monitorPoint;      //属于的监测点
    private Zone zone;                 //所属区域
    private String placeCode;           //点位编码
    private String placeName;           //点位名称
    private String placeInfo;           //点位信息
    private Date createTime;            //摄像机添加时间
    private String dvIp;                //摄像机IP
    private float imageRealWidth;       //图片实景宽度， 单位(mm 毫米)
    private int imageWidth;     //图片宽度,单位像素
    private int imageHeight;    //图片高度，单位像素
    private FTPProfile ftp;     //Ftp信息
    private boolean enable;     //是否启用
    private String remark;      //备注
    private PictureBean pictureBean;  //最新一张图片

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MonitorPointBean getMonitorPoint() {
        return monitorPoint;
    }

    public void setMonitorPoint(MonitorPointBean monitorPoint) {
        this.monitorPoint = monitorPoint;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public String getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(String placeCode) {
        this.placeCode = placeCode;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDvIp() {
        return dvIp;
    }

    public void setDvIp(String dvIp) {
        this.dvIp = dvIp;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public FTPProfile getFtp() {
        return ftp;
    }

    public void setFtp(FTPProfile ftp) {
        this.ftp = ftp;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getImageRealWidth() {
        return imageRealWidth;
    }

    public void setImageRealWidth(float imageRealWidth) {
        this.imageRealWidth = imageRealWidth;
    }

    public PictureBean getPictureBean() {
        return pictureBean;
    }

    public void setPictureBean(PictureBean pictureBean) {
        this.pictureBean = pictureBean;
    }
}