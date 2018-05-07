package com.microwise.proxima.infraredImageResolution;

/**
 * 表示某个区域
 * <p/>
 * start 为区域的左上角, end 为区域的右下角 Date: 12-8-24 Time: 下午2:54
 * 
 * @author bastengao
 * @check guo.tian li.jianfei 2012-09-19
 */
public class Dimension {
	private int startX;
	private int startY;
	private int endX;
	private int endY;

	public Dimension(int startX, int startY, int endX, int endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	/**
	 * 判断某个点是否在这个区域内
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean in(int x, int y) {
		if (startX <= x && x <= endX) {
			if (startY <= y && y <= endY) {
				return true;
			}
		}
		return false;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}
}
