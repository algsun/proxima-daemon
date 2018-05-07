package com.microwise.proxima.infraredImageResolution;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 12-8-20 Time: 下午4:38
 *
 * @author bastengao
 */
public class InfraredImage {
    public static final List<Dimension> blackDimensions = initBlackDimensions();

    private static List<Dimension> initBlackDimensions() {
        List<Dimension> blackDimensions = new ArrayList<Dimension>();
        blackDimensions.add(new Dimension(10, 3, 40, 35)); //左上角，摄氏单位
        blackDimensions.add(new Dimension(553, 3, 635, 40)); //右上角，最高温度
        blackDimensions.add(new Dimension(613, 40, 635, 440)); //右侧，温度比例尺
        blackDimensions.add(new Dimension(553, 438, 635, 475)); //右下角，最低温度
        blackDimensions.add(new Dimension(3, 447, 302, 474)); //左下角，label
        return blackDimensions;
    }

    /**
     * 判断是否在黑名单中
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean inBlackDimensions(int x, int y) {
        for (Dimension dimension : blackDimensions) {
            if (dimension.in(x, y)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 抽取顶部颜色数字
     *
     * @param infraredImage
     * @return
     */
    public static BufferedImage extractTopLabel(BufferedImage infraredImage) {
        int startX = 556;
        int startY = 5;
        int endX = 634;
        int endY = 39;

        int width = endX - startX;
        int height = endY - startY;

        int[] rgbs = infraredImage.getRGB(startX, startY, width, height, null, 0, width);

        BufferedImage topLabel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        topLabel.setRGB(0, 0, width, height, rgbs, 0, width);
        return topLabel;
    }

    /**
     * 抽取底部颜色数字
     *
     * @param infraredImage
     * @return
     */
    public static BufferedImage extractBottomLabel(BufferedImage infraredImage) {
        int startX = 555;
        int startY = 440;
        int endX = 634;
        int endY = 474;

        int width = endX - startX;
        int height = endY - startY;

        int[] rgbs = infraredImage.getRGB(startX, startY, width, height, null, 0, width);

        BufferedImage topLabel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        topLabel.setRGB(0, 0, width, height, rgbs, 0, width);
        return topLabel;
    }

}
