package com.microwise.proxima.bean;

import com.microwise.proxima.bean.base.NumberEnum;

/**
 * 摄像机类型
 * 
 * @author gaohui
 * @date 2012-7-6
 */
public enum DVType implements NumberEnum {
	/**
	 * 光学摄像机
	 */
	OPTICS(1),
	/**
	 * 红外摄像机
	 */
	INFRARED(2);

	private int number;

	private DVType(int number) {
		this.number = number;
	}

	@Override
	public int number() {
		return this.number;
	}
}
