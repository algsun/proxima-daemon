package com.microwise.proxima.bean;


/**
 * 红外图片数据
 * 
 * @author li.jianfe
 * @date 2012-09-03
 */
public class InfraredPictureDataBean {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 图片
	 */
	private PictureBean picture;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PictureBean getPicture() {
		return picture;
	}

	public void setPicture(PictureBean picture) {
		this.picture = picture;
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

}
