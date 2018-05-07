package com.microwise.proxima.infraredImageResolution.scheduler;

import com.microwise.proxima.bean.InfraredDVPlaceBean;
import com.microwise.proxima.bean.InfraredMarkRegionBean;
import com.microwise.proxima.bean.InfraredPictureDataBean;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.infraredImageResolution.ColorWheel;
import com.microwise.proxima.infraredImageResolution.InfraredAnalyzer;
import com.microwise.proxima.service.InfraredMarkRegionDataService;
import com.microwise.proxima.service.InfraredMarkRegionService;
import com.microwise.proxima.service.InfraredPictureDataService;
import com.microwise.proxima.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 红外图片新区域解析任务计划主类
 *
 * @author zhang.licong
 * @date 2012-9-6
 * @check guo.tian li.jianfei 2012-09-19
 */
@Component
@Scope("prototype")
public class NewRegionsCalculateMain {

    public static final Logger log = LoggerFactory
            .getLogger(NewRegionsCalculateMain.class);

    /**
     * 红外标记区域 service
     */
    @Autowired
    private InfraredMarkRegionService infraredMarkRegionService;

    /**
     * 红外图片区域数据服务层
     */
    @Autowired
    private InfraredMarkRegionDataService infraredMarkRegionDataService;

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
     * 红外图片新区域解析主方法
     *
     * @throws Exception
     * @author zhang.licong
     * @date 2012-9-10
     */
    public void mainExecutive(InfraredDVPlaceBean infDVPlaceBean,
                              AtomicBoolean interrupted) throws Exception {

        if (infDVPlaceBean == null) {
            return;
        }

        // 获取摄像机点位区域
        List<InfraredMarkRegionBean> infList = this.infraredMarkRegionService
                .findInfraredMarkRegions(infDVPlaceBean.getId());

        // 判断是否有区域
        if (infList == null || infList.size() == 0) {
            return;
        }

        for (InfraredMarkRegionBean infraredMarkRegionBean : infList) {

            // 根据区域信息获取没有分析的图片进行区域分析
            List<PictureBean> picList = this.infraredMarkRegionDataService
                    .findNoResolutionPictures(infraredMarkRegionBean.getId(),
                            infraredMarkRegionBean.getDvPlace().getId());

            // 对图片进行遍历解析
            for (PictureBean pictureBean : picList) {

                // 判断是否打断
                if (interrupted.get()) {
                    log.info("中断任务执行总方法。");
                    return;
                }
                try {
                    TimeUnit.SECONDS.sleep(15);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                log.info("加载图片信息");
                // 获取图片路径
                String path = new File(PathUtil.getWebInfPath2()).getPath();
                File file = new File(path + "/" + pictureBean.getPath(),
                        pictureBean.getName());

                // 获取图片的高低温
                InfraredPictureDataBean infraredPictureData = this.infraredPictureDataService
                        .findByPicId(pictureBean.getId());

                // 判断高低温是否为空，如果为空解析图片的高低温
                if (infraredPictureData == null) {
                    try {
                        infraredPictureData = this.infraredAnalyzer
                                .saveInfraredPicData(pictureBean, file);
                    } catch (Exception e) {
                        log.error(String.format("解析文物图片(%s/%s)高、低、平均温度失败.",
                                pictureBean.getPath(), pictureBean.getName()),
                                e);
                        continue;
                    }
                }

                // 获取色轮
                ColorWheel colorWheel = this.infraredAnalyzer.getColorWheel();

                // 解析图片
                this.infraredAnalyzer.infraredSingleRegionAnalyzer(
                        infraredMarkRegionBean, infraredPictureData, file,
                        colorWheel, pictureBean);
            }
        }
        log.info("执行完成");
    }
}
