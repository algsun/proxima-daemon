package com.microwise.proxima.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 坐标相关工具类
 * 
 * @author gaohui
 * @date 2012-8-9
 */
public class PositionUtil {
	private PositionUtil() {
	}

	/**
	 * 计算两点距离
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @return
	 */
	public static double length(int startX, int startY, int endX, int endY) {
		return Math.sqrt(Math.pow(startX - endX, 2)
				+ Math.pow(startY - endY, 2));
	}

	/**
	 * 实际长度
	 * 
	 * @param lengthPixel
	 * @param imageRealWidth
	 * @param imageSrcWidth
	 * @return
	 */
	public static float realLength(double lengthPixel, float imageRealWidth,
			int imageSrcWidth) {
		double lengthOfMM = lengthPixel * imageRealWidth / imageSrcWidth;
		return PositionUtil.roundFloat(lengthOfMM, 2); // 保留两个小数，最后一位四舍五入
	}

	/**
	 * 四舍五入
	 * 
	 * @param value
	 * @param precision
	 * @return
	 */
	public static double round(double value, int precision) {
		return BigDecimal.valueOf(value)
				.setScale(precision, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 四舍五入
	 * 
	 * @param value
	 * @param precision
	 * @return
	 */
	public static float roundFloat(double value, int precision) {
		return BigDecimal.valueOf(value)
				.setScale(precision, RoundingMode.HALF_UP)
                .floatValue();
	}

    /**
     * 四舍五入
     *
     * @param value
     * @param precision
     * @return
     */
    public static float roundFloat(float value, int precision) {
        return BigDecimal.valueOf(value)
                .setScale(precision, RoundingMode.HALF_UP)
                .floatValue();
    }

}
