package com.microwise.proxima.bean.base;

/**
 * NumberEnum 工具类
 * 
 * @author gaohui
 * @date 2012-07-02
 */
public class NumberEnums {
	private NumberEnums() {
	};

	/**
	 * 通过 number 转化为对应的 枚举类型
	 * 
	 * @param <E>
	 * @param number
	 * @param clazz
	 * @return
	 */
	public static <E extends NumberEnum> E valueOf(int number, Class<E> clazz) {
		if (!clazz.isEnum()) {
			throw new IllegalArgumentException(" clazz is not enum type.");
		}

		E[] enums = clazz.getEnumConstants();
		if (enums != null) {
			for (E e : enums) {
				if (e.number() == number) {
					return e;
				}
			}
		}
		return null;
	}

}
