package com.microwise.proxima.bean;

/**
 * 区域
 *
 * @author gaohui
 * @date 13-3-18 15:26
 */
public class Zone {
    /**
     * ID
     */
    private String id;
    /**
     * 区域名称
     */
    private String name;
    /**
     * 实景图
     */
    private String planImage;

    /**
     * 数据同步版本
     */
    private long dataVersion;

    /**
     * 父级区域
     */
    private Zone parent;
    /**
     * 所属站点
     */
    private Site site;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanImage() {
        return planImage;
    }

    public void setPlanImage(String planImage) {
        this.planImage = planImage;
    }

    public long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(long dataVersion) {
        this.dataVersion = dataVersion;
    }

    public Zone getParent() {
        return parent;
    }

    public void setParent(Zone parent) {
        this.parent = parent;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
