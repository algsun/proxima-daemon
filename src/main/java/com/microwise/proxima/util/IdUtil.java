package com.microwise.proxima.util;

import java.util.UUID;

/**
 * ID生成策略工具类
 * 
 * @author zhangpeng
 * @date 2013-3-18
 */
public class IdUtil {

	/** 0到9，a到z，A到Z，-，_，64进制字符 */
	public static final char[] charArray;

	static {
		charArray = new char[64];
		for (int i = 0; i < 10; i++) {
			charArray[i] = (char) ('0' + i);
		}
		for (int i = 10; i < 36; i++) {
			charArray[i] = (char) ('a' + i - 10);
		}
		for (int i = 36; i < 62; i++) {
			charArray[i] = (char) ('A' + i - 36);
		}
		charArray[62] = '_';
		charArray[63] = '-';
	}

	public static String hexTo64(String hex) {
		StringBuffer r = new StringBuffer();
		int index = 0;
		int[] buff = new int[3];
		int l = hex.length();
		for (int i = 0; i < l; i++) {
			index = i % 3;
			buff[index] = Integer.parseInt("" + hex.charAt(i), 16);
			if (index == 2) {
				r.append(charArray[buff[0] << 2 | buff[1] >>> 2]);
				r.append(charArray[(buff[1] & 3) << 4 | buff[2]]);
			}
		}
		return r.toString();
	}

	/**
	 * 获取一个22位长的64进制UUID
	 * 
	 * @author zhangpeng
	 * @date 2013-3-18
	 * 
	 * @return String
	 */
	public static String get64UUID() {
		String uuid = UUID.randomUUID().toString();
		// 把36位的UUID，去掉“-”变成32位的16进制数。
		uuid = uuid.replaceAll("-", "").toUpperCase();
		// 在这个数前补一个16进制数0，就变成了33位（132个二进制数，33*4）。
		uuid = "0" + uuid;
		// 把这个33位的16进制数，转换成22位的64进制数。
		return hexTo64(uuid);
	}

	public static void main(String args[]) {
		System.out.println(get64UUID());
	}

}
