package com.microwise.proxima.bean;

import com.microwise.proxima.beans2.IdentityCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 图片
 * 
 * @author gaohui
 * @date 2012-7-6
 */
public class PictureBean {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 图片名称
	 */
	private String name;

	/**
	 * 图片路径
	 */
	private String path;

	/**
	 * 保存时间
	 */
	private Date saveTime;

    /**
     * 是否可以解析温度
     */
    private boolean isAnalyzable;

	/**
	 * 属于的摄像机点位
	 */
	private DVPlaceBean dv;

    /**
     * 图片标记段
     */
    private List<IdentityCode> identityCodeList = new ArrayList<IdentityCode>();

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

    public boolean isAnalyzable() {
        return isAnalyzable;
    }

    public void setAnalyzable(boolean analyzable) {
        isAnalyzable = analyzable;
    }

    public DVPlaceBean getDv() {
		return dv;
	}

	public void setDv(DVPlaceBean dv) {
		this.dv = dv;
	}

    public List<IdentityCode> getIdentityCodeList() {
        return identityCodeList;
    }

    public void setIdentityCodeList(List<IdentityCode> identityCodeList) {
        this.identityCodeList = identityCodeList;
    }
}
