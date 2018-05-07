package com.microwise.proxima.imagesync.ext;

import com.microwise.proxima.bean.*;
import com.microwise.proxima.infraredImageResolution.ColorWheel;
import com.microwise.proxima.infraredImageResolution.InfraredAnalyzer;
import com.microwise.proxima.service.InfraredMarkRegionService;
import com.microwise.proxima.service.InfraredPictureDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * 红外分析图片监听.
 *
 * @author wang.geng
 * @date 14-4-2 上午9:49
 * @check li.jianfei liu.zhu 2014-4-15 #8263
 */
@Component
@Scope("prototype")
public class InfraredAnalyzerListener implements ImageSyncListener {

    private static final Logger log = LoggerFactory.getLogger(InfraredAnalyzerListener.class);

    /**
     * 红外图片解析类
     */
    @Autowired
    private InfraredAnalyzer infraredAnalyzer;

    /**
     * 红外图片数据信息服务层
     */
    @Autowired
    private InfraredPictureDataService infraredPictureDataService;

    /**
     * 红外标记区域 service
     */
    @Autowired
    private InfraredMarkRegionService infraredMarkRegionService;

    @Override
    public void onImageSync(File newImage, DVType dvType, DVPlaceBean dvPlace, PictureBean pictureBean) {
        //如果不是红外图片，直接返回
        if (dvType != DVType.INFRARED) {
            return;
        }

        //分析原图的图片温度
        InfraredPictureDataBean infraredPictureDataBean = this.infraredPictureDataService
                .findByPicId(pictureBean.getId());

        //判断高低温度是否温控，为空则解析图片的高低温
        if (infraredPictureDataBean == null) {
            try {
                infraredPictureDataBean = this.infraredAnalyzer.saveInfraredPicData(pictureBean, newImage);
            } catch (Exception e) {
                log.error(String.format("解析文物图片(%s/%s)高、低、平均温度失败.",
                        pictureBean.getPath(), pictureBean.getName()), e);
            }
        }

        //获取摄像机点位区域
        List<InfraredMarkRegionBean> infraredList = this.infraredMarkRegionService
                .findInfraredMarkRegions(dvPlace.getId());

        //判断是否有区域
        if (infraredList == null) {
            return;
        }

        for (InfraredMarkRegionBean infraredMark : infraredList) {

            ColorWheel colorWheel = this.infraredAnalyzer.getColorWheel();

            //解析图片
            try {
                this.infraredAnalyzer.infraredSingleRegionAnalyzer(
                        infraredMark, infraredPictureDataBean, newImage, colorWheel, pictureBean
                );
            } catch (Exception e) {
                log.error("解析图片失败", e);
            }
        }
        log.info("执行完成");
    }
}
