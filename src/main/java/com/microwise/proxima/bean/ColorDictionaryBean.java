package com.microwise.proxima.bean;

/**
 * 色轮字典
 * 
 * @author li.jianfei
 * @date 2012-09-03
 */
public class ColorDictionaryBean {
	/**
	 * ID
	 */
	private int id;
	
	/**
	 * 色轮索引
	 */
	private int colorIndex;
	
	/**
	 * 色轮RGB
	 */
	private int colorRGB;
	
	/**
	 * 色轮类型
	 */
	private int colorType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getColorIndex() {
		return colorIndex;
	}

	public void setColorIndex(int colorIndex) {
		this.colorIndex = colorIndex;
	}

	public int getColorRGB() {
		return colorRGB;
	}

	public void setColorRGB(int colorRGB) {
		this.colorRGB = colorRGB;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

}
