package com.microwise.proxima.bean;

/**
 * 物理站点Bean
 *
 * @author zhangpeng
 * @date 2012-11-15
 */
public class Site {

    /**
     * 物理站点唯一标识
     */
    private String siteId;

    /**
     * 物理站点名称
     */
    private String siteName;

    /**
     * 所属区域
     */
    private int areaCode;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }
}
