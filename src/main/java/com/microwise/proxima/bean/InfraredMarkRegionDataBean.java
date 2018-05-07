package com.microwise.proxima.bean;


/**
 * 红外标记区域数据(温度) 实体
 * 
 * @author gaohui
 * @date 2012-9-5
 */
public class InfraredMarkRegionDataBean {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 最高温度
	 */
	private double highTemperature;

	/**
	 * 最低温度
	 */
	private double lowTemperature;

	/**
	 * 平均温度
	 */
	private double averageTemperature;

	/**
	 * 对应图片
	 */
	private PictureBean picture;

	/**
	 * 对应的标记区域
	 */
	private InfraredMarkRegionBean markRegion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getHighTemperature() {
		return highTemperature;
	}

	public void setHighTemperature(double highTemperature) {
		this.highTemperature = highTemperature;
	}

	public double getLowTemperature() {
		return lowTemperature;
	}

	public void setLowTemperature(double lowTemperature) {
		this.lowTemperature = lowTemperature;
	}

	public double getAverageTemperature() {
		return averageTemperature;
	}

	public void setAverageTemperature(double averageTemperature) {
		this.averageTemperature = averageTemperature;
	}

	public void setAverageTemperature(float averageTemperature) {
		this.averageTemperature = averageTemperature;
	}

	public PictureBean getPicture() {
		return picture;
	}

	public void setPicture(PictureBean picture) {
		this.picture = picture;
	}

	public InfraredMarkRegionBean getMarkRegion() {
		return markRegion;
	}

	public void setMarkRegion(InfraredMarkRegionBean markRegion) {
		this.markRegion = markRegion;
	}

}
