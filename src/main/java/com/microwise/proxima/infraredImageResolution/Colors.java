package com.microwise.proxima.infraredImageResolution;

import com.google.common.base.Strings;

/**
 * Date: 12-8-20 Time: 下午5:16
 * 
 * @author bastengao
 * 
 * @check guo.tian li.jianfei 2012-09-19
 */
public class Colors {
	/**
	 * 返回颜色的16进制表示
	 * 
	 * @param rgb
	 * @return
	 */
	public static String toHex(int rgb) {
		return Strings.padStart(Integer.toHexString(rgb).toUpperCase(), 6, '0');
	}

	/**
	 * <p>
	 * 从十六进制转换(such as : A1B2C3)为 int
	 * </p>
	 * 
	 * @param colorHexFormat
	 * @return
	 */
	public static int fromHex(String colorHexFormat) {
		return Integer.parseInt(colorHexFormat, 16);
	}

	/**
	 * 返回颜色的 10 进制表示
	 * 
	 * @param rgb
	 * @return
	 */
	public static String toDecimal(int rgb) {
		int[] rgbs = flat(rgb);
		return String.format("(%d,%d,%d)", rgbs[0], rgbs[1], rgbs[2]);
	}

	/**
	 * 合并颜色
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static int merge(int red, int green, int blue) {
		return (red << 16) + (green << 8) + blue;
	}

	/**
	 * 返回 new int[]{red, green, blue}
	 * 
	 * @param rgb
	 * @return
	 */
	public static int[] flat(int rgb) {
		int rgbNoAlpha = noAlpha(rgb);
		int red = (rgbNoAlpha & 0xFF0000) >> 16;
		int green = (rgbNoAlpha & 0x00FF00) >> 8;
		int blue = (rgbNoAlpha & 0x0000FF);
		return new int[] { red, green, blue };
	}

	/**
	 * 去掉最高位的 alpha
	 * 
	 * @param argb
	 * @return
	 */
	public static int noAlpha(int argb) {
		return (argb & 0x00FFFFFF);
	}

	/**
	 * 判断两个颜色是否接近. RGB 三个分量都小于 delta 时返回 true.( TODO 为了更精确，可以考虑使用向量之间的笛卡尔积)
	 * 
	 * @param rgb
	 * @param rgb2
	 * @param delta
	 * @return
	 */
	public static boolean isSimilar(int rgb, int rgb2, int delta) {
		int[] rgbs = flat(rgb);
		int[] rgbs2 = flat(rgb2);

		if (Math.abs(rgbs[0] - rgbs2[0]) > delta) {
			return false;
		}

		if (Math.abs(rgbs[1] - rgbs2[1]) > delta) {
			return false;
		}

		if (Math.abs(rgbs[2] - rgbs2[2]) > delta) {
			return false;
		}

		return true;
	}
}
