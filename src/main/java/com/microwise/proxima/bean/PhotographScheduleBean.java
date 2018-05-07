package com.microwise.proxima.bean;


/**
 * 拍照计划
 * 
 * @author gaohui
 * @date 2012-7-31
 */
public class PhotographScheduleBean {
	/**
	 * ID
	 */
	private String id;

	/**
	 * 属于的摄像机点位
	 */
	private DVPlaceBean dvPlace;

	/**
	 * 哪一天拍照
	 */
	private DayType dayOfWeek;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DVPlaceBean getDvPlace() {
		return dvPlace;
	}

	public void setDvPlace(DVPlaceBean dvPlace) {
		this.dvPlace = dvPlace;
	}

	public DayType getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayType dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

}
