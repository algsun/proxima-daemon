package com.microwise.proxima.bean;

import java.util.Date;

/**
 * 标记段
 * 
 * @author gaohui
 * @date 2012-7-12
 */
public class MarkSegmentBean {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 标记名称
	 */
	private String name;

	/**
	 * 摄像机点位
	 */
	private DVPlaceBean dvPlace;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 注销时间
	 */
	private Date cancelTime;

	/**
	 * 是否注销
	 */
	private boolean cancel;

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

	public DVPlaceBean getDvPlace() {
		return dvPlace;
	}

	public void setDvPlace(DVPlaceBean dvPlace) {
		this.dvPlace = dvPlace;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

}
