package com.microwise.proxima.util;

/**
 * spring bean scope 枚举 Date: 12-9-27 Time: 下午6:39
 * 
 * @author gaohui
 */
public class BeanScopes {

	private BeanScopes() {
	}

	/**
	 * 单例
	 */
	public static final String SINGLETON = "singleton";

	/**
	 * 原型
	 */
	public static final String PROTOTYPE = "prototype";
}
