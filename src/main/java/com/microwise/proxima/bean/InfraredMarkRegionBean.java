package com.microwise.proxima.bean;

/**
 * 红外标记区域 实体
 * 
 * @author gaohui
 * @date 2012-9-5
 */
public class InfraredMarkRegionBean {

	/**
	 * ID
	 */
	private String id;
	
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 摄像机点位
	 */
	private DVPlaceBean dvPlace;

	/**
	 * 区域左上角 x
	 */
	private int positionX;

	/**
	 * 区域左上角 y
	 */
	private int positionY;

	/**
	 * 区域宽度
	 */
	private int regionWidth;

	/**
	 * 区域高度
	 */
	private int regionHeight;

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

	public int getRegionWidth() {
		return regionWidth;
	}

	public void setRegionWidth(int regionWidth) {
		this.regionWidth = regionWidth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRegionHeight() {
		return regionHeight;
	}

	public void setRegionHeight(int regionHeight) {
		this.regionHeight = regionHeight;
	}

}
