package com.microwise.proxima.infraredImageResolution;

import com.google.common.base.Strings;
import com.microwise.proxima.bean.InfraredMarkRegionBean;
import com.microwise.proxima.bean.InfraredMarkRegionDataBean;
import com.microwise.proxima.bean.InfraredPictureDataBean;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.service.InfraredMarkRegionDataService;
import com.microwise.proxima.service.InfraredPictureDataService;
import com.microwise.proxima.util.IdUtil;
import com.microwise.proxima.util.PositionUtil;
import com.microwise.proxima.util.ocr.TesseractOCR;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 红外图片解析类
 *
 * @author li.jianfei
 * @date 2012-09-05
 * @check guo.tian li.jianfei 2012-09-19
 * @check li.jianfei liu.zhu #8263 2014-4-15
 */
@Component
@Scope("prototype")
public class InfraredAnalyzer {
    public static final Logger log = Logger.getLogger(InfraredAnalyzer.class);

    /**
     * 红外图片数据Service注入
     */
    @Autowired
    private InfraredPictureDataService infraredPictureDataService;

    /**
     * 红外图片区域数据表service注入
     */
    @Autowired
    private InfraredMarkRegionDataService infraredMarkRegionDataService;

    /**
     * 从数据库中加载 ColorWheel接口
     */
    @Autowired
    private ColorWheelHolder colorWheelHolder;

    /**
     * 保留 1 位小数
     */
    private static final int PRECISION = 1;

    /**
     * 获取色轮索引时的上下浮动范围
     */
    private static final int MAX_DELTA = 10;

    /**
     * 解析并保存红外图片高低温数据
     *
     * @param picture
     * @param file
     * @author li.jianfei
     * @date 2012-09-03
     */
    public InfraredPictureDataBean saveInfraredPicData(PictureBean picture,
                                                       File file) throws Exception {
        int width = 0; // 红外图片宽度
        int height = 0; // 红外图片高度
        int count = 0; // 红外图片上的有效温度数
        double sumTemperature = 0; // 有效温度总和


        // 解析红外图片最高温度
        InputStream inputStream = new FileInputStream(file);
        BufferedImage image = ImageIO.read(inputStream);
        inputStream.close();
        BufferedImage topLabel = InfraredImage.extractTopLabel(image);
        String topTem = TesseractOCR.recognize(topLabel);


        // 解析红外图片最低温度
        BufferedImage bottomLabel = InfraredImage.extractBottomLabel(image);
        String bottomTem = TesseractOCR.recognize(bottomLabel);

        // 红外图像数据对象
        InfraredPictureDataBean infraredPictureData = new InfraredPictureDataBean();
        //设置红外图像数据对象UUID
        infraredPictureData.setId(IdUtil.get64UUID());
        // 设置 picId
        infraredPictureData.setPicture(picture);
        try {
            infraredPictureData.setHighTemperature(PositionUtil.round(
                    Double.parseDouble(topTem), PRECISION));
            infraredPictureData.setLowTemperature(PositionUtil.round(
                    Double.parseDouble(bottomTem), PRECISION));
        } catch (Exception e) {
            topTem = null;
            bottomTem = null;
        }

        // 计算红外图片的平均温度
        // 最高温度或者最低温度解析不了，那么都置为 0 .判断图片是否能被解析通过最高温度和最低温度都为 0 判断
        if (Strings.isNullOrEmpty(topTem) || Strings.isNullOrEmpty(bottomTem)) {
        } else {

            // 根据色轮字典创建色轮图片
            ColorWheel colorWheel = getColorWheel();

            // 获取红外图片宽度和高度
            width = image.getWidth();
            height = image.getHeight();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (!InfraredImage.inBlackDimensions(x, y)) {
                        // 获取当前点在色轮中的索引
                        int rgb = image.getRGB(x, y);
                        int heightOfImageWheel = colorWheel
                                .closestHeightOfColor(rgb, MAX_DELTA);

                        // 根据当前索引获取对应的高度
                        int imageWheelMaxHeight = colorWheel.getRgbSAverage().length;

                        // 返回值为-1时不计算该点温度
                        if (heightOfImageWheel == -1) {
                            continue;
                        }
                        double rate = (imageWheelMaxHeight - heightOfImageWheel)
                                / (float) imageWheelMaxHeight;
                        double value = rate
                                * (infraredPictureData.getHighTemperature() - infraredPictureData
                                .getLowTemperature())
                                + infraredPictureData.getLowTemperature();

                        // 计数器及温度总和处理
                        count++;
                        sumTemperature = sumTemperature + value;
                    }
                }
            }
        }
        if (sumTemperature != 0.0) {
            sumTemperature = sumTemperature / count;
        }
        infraredPictureData.setAverageTemperature(PositionUtil.round(
                sumTemperature, PRECISION));

        log.debug("红外图片最高温度：" + infraredPictureData.getHighTemperature());
        log.debug("红外图片最低温度：" + infraredPictureData.getLowTemperature());
        log.debug("红外图片平均温度：" + infraredPictureData.getAverageTemperature());
        infraredPictureDataService.save(infraredPictureData);
        log.debug("保存红外图片数据到数据库....");
        return infraredPictureData;
    }

    /**
     * 计算单个红外区域的高、低、平均温度
     *
     * @param inf
     * @param infraredPictureData
     * @param file
     * @param colorWheel
     * @param picture
     * @throws Exception
     * @author zhang.licong
     * @date 2012-9-6
     */
    public void infraredSingleRegionAnalyzer(InfraredMarkRegionBean inf,
                                             InfraredPictureDataBean infraredPictureData, File file,
                                             ColorWheel colorWheel, PictureBean picture) throws Exception {
        // 如果图片不被解析, 则直接设置当前区域的温度都为 0
        if (infraredPictureData.getHighTemperature() == 0 && infraredPictureData.getLowTemperature() == 0) {
            InfraredMarkRegionDataBean imrd = new InfraredMarkRegionDataBean();
            imrd.setId(IdUtil.get64UUID());
            imrd.setHighTemperature(0);
            imrd.setLowTemperature(0);
            imrd.setAverageTemperature(0);
            imrd.setPicture(picture);
            imrd.setMarkRegion(inf);
            infraredMarkRegionDataService.save(imrd);
            return;
        }


        // 加载图片信息
        InputStream inputStream = new FileInputStream(file);
        BufferedImage image = ImageIO.read(inputStream);
        inputStream.close();

        // 获取红外图片的高度
        int pointx = inf.getPositionX();
        int pointy = inf.getPositionY();
        int pointWidth = inf.getRegionWidth();
        int pointHeight = inf.getRegionHeight();

        int count = 0;// 区域点数
        double sumTemperature = 0.00;// 区域温度总和
        double highTemperature;// 高温
        double lowTemperature;// 低温
        double averageTemperature;// 平均温度

        List<Double> listValue = new ArrayList<Double>();

        // 遍历图片中的区域，并获取当前点温度
        for (int x = pointx; x < pointx + pointWidth; x++) {
            for (int y = pointy; y < pointy + pointHeight; y++) {

                if (!InfraredImage.inBlackDimensions(x, y)) {// 去除黑名单的像素
                    // 获取当前点在色轮中的索引
                    int rgb = image.getRGB(x, y);
                    int heightOfImageWheel = colorWheel.closestHeightOfColor(
                            rgb, MAX_DELTA);

                    // 根据当前索引获取对应的高度
                    int imageWheelMaxHeight = colorWheel.getRgbSAverage().length;

                    // 返回值为-1时不计算该点温度
                    if (heightOfImageWheel == -1) {
                        continue;
                    }

                    // 求出色轮百分比
                    double rate = (imageWheelMaxHeight - heightOfImageWheel)
                            / (float) imageWheelMaxHeight;

                    // 求出当前点的温度
                    double value = rate
                            * (infraredPictureData.getHighTemperature() - infraredPictureData
                            .getLowTemperature())
                            + infraredPictureData.getLowTemperature();
                    listValue.add(value);

                    // 计数器及温度总和处理
                    count++;
                    sumTemperature = sumTemperature + value;
                }
            }
        }
        // 计算区域高低温
        Collections.sort(listValue);

        // 温度保留1位小数

        highTemperature = PositionUtil.round(
                listValue.get(listValue.size() - 1), PRECISION);// 高温
        lowTemperature = PositionUtil.round((listValue.get(0)), PRECISION);// 低温
        averageTemperature = PositionUtil.round(sumTemperature / count,
                PRECISION);// 平均温度
        InfraredMarkRegionDataBean imrd = new InfraredMarkRegionDataBean();
        imrd.setId(IdUtil.get64UUID());
        imrd.setHighTemperature(highTemperature);
        imrd.setLowTemperature(lowTemperature);
        imrd.setAverageTemperature(averageTemperature);
        imrd.setPicture(picture);
        imrd.setMarkRegion(inf);
        // 数据库保存
        infraredMarkRegionDataService.save(imrd);
    }

    /**
     * 根据色轮字典创建色轮图片
     *
     * @author zhang.licong
     * @date 2012-9-6
     */
    public ColorWheel getColorWheel() {
        return colorWheelHolder.getColorWheel();
    }

}
