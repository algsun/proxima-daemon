package com.microwise.proxima.infraredImageResolution;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Date: 12-8-20 Time: 下午4:47
 * 
 * @author bastengao
 * 
 * @check guo.tian li.jianfei 2012-09-19
 */
public class ColorWheel {
	private int[] rgbSAverage = null;

	/**
	 * 通过传入数据库色轮字典初始化ColorWheel类
	 * 
	 * @param rgbAverge
	 */
	public ColorWheel(int[] rgbAverge) {
		this.rgbSAverage = rgbAverge;
	}

	public ColorWheel(BufferedImage colorWheelImage) {
		int width = colorWheelImage.getWidth();
		int height = colorWheelImage.getHeight();

		rgbSAverage = new int[height];
		// 逐行扫描, 求每行颜色的平均值
		for (int heightIndex = 0; heightIndex < height; heightIndex++) {
			// 去掉每行第一个像素和最后一个像素
			int[] rowRGB = colorWheelImage.getRGB(1, heightIndex, width - 2, 1,
					null, 0, width - 2);
			// System.out.printf("row [%3s]", heightIndex);
			int redSum = 0;
			int greenSum = 0;
			int blueSum = 0;
			for (int rgb : rowRGB) {
				int[] rgbNoAlpha = Colors.flat(rgb);
				int red = rgbNoAlpha[0];
				int green = rgbNoAlpha[1];
				int blue = rgbNoAlpha[2];
				redSum += red;
				greenSum += green;
				blueSum += blue;
			}

			int redAverage = redSum / rowRGB.length;
			int greenAverage = greenSum / rowRGB.length;
			int blueAverage = blueSum / rowRGB.length;
			int rgbAverage = Colors
					.merge(redAverage, greenAverage, blueAverage);
			rgbSAverage[heightIndex] = rgbAverage;
		}
	}

	/**
	 * 全高
	 * 
	 * @return
	 */
	public int height() {
		return rgbSAverage.length;
	}

	/**
	 * 返回颜色拷贝
	 * 
	 * @return
	 */
	public int[] getRgbSAverage() {
		return Arrays.copyOf(rgbSAverage, rgbSAverage.length);
	}

	/**
	 * 对应颜色的高度, 默认 delta 为 5
	 * 
	 * @param rgb
	 * @return
	 */
	public int heightOfColor(int rgb) {
		for (int i = 0; i < rgbSAverage.length; i++) {
			if (Colors.isSimilar(rgb, rgbSAverage[i], 5)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 根据颜色的 delta 值，找到最相近的第一个颜色的高度
	 * 
	 * @param rgb
	 * @param maxDelta
	 * @return
	 */
	public int heightOfColor(int rgb, int maxDelta) {
		for (int i = 0; i < rgbSAverage.length; i++) {
			if (Colors.isSimilar(rgb, rgbSAverage[i], maxDelta)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 找出最接近的高度
	 * 
	 * @param rgb
	 * @return
	 */
	public int closestHeightOfColor(int rgb) {
		int initDelta = 3;
		int delta = 3; // default delta is 3. hit rate 80%

		int lastHeight = -1;
		int lastDelta = initDelta;
		int height = heightOfColor(rgb, delta);
		if (height == -1) { // 放大
			while (height == -1) {
				delta += 1;
				height = heightOfColor(rgb, delta);
			}
			lastDelta = delta;
			lastHeight = height;
		} else { // 缩小
			do {
				lastDelta = delta;
				lastHeight = height;
				delta -= 1;
				height = heightOfColor(rgb, delta);
			} while (delta >= 0 && height != -1);
		}

		deltaCount.add(lastDelta);
		return lastHeight;
	}

	/**
	 * 在 maxDelta 允许内，找出接近的颜色高度
	 * 
	 * @param rgb
	 * @param maxDelta
	 * @return
	 */
	public int closestHeightOfColor(int rgb, int maxDelta) {
		int initDelta = 3;
		int delta = 3; // default delta is 3. hit rate 80%

		int lastHeight = -1;
		int lastDelta = initDelta;
		int height = heightOfColor(rgb, delta);
		if (height == -1) { // 放大
			while (height == -1 && delta < maxDelta) {
				delta += 1;
				height = heightOfColor(rgb, delta);
			}
			lastDelta = delta;
			lastHeight = height;
		} else { // 缩小
			do {
				lastDelta = delta;
				lastHeight = height;
				delta -= 1;
				height = heightOfColor(rgb, delta);
			} while (delta >= 0 && height != -1);
		}

		if (lastHeight != -1) {
			deltaCount.add(lastDelta);
		}
		return lastHeight;
	}

	@VisibleForTesting
	public Multiset<Integer> deltaCount = HashMultiset.create();
}
