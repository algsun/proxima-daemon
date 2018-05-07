package com.microwise.proxima.imagesync.ext;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.DVType;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.service.DVPlaceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 同步图片大小
 *
 * @author gaohui
 * @date 13-6-19 17:46
 */
public class SyncImageSizeListener implements ImageSyncListener {
    @Autowired
    private DVPlaceService dvPlaceService;

    @Override
    public void onImageSync(File newImage, DVType dvType, DVPlaceBean dvPlace, PictureBean pictureBean) {
        // 如果摄像机点位的 图片大小已经同上过, 图片大小都不为零, 则不需要同步
        if (dvPlace.getImageWidth() != 0 && dvPlace.getImageHeight() != 0) {
            return;
        }

        DVPlaceBean dvPlaceNew = dvPlaceService.findById(dvPlace.getId());
        // 图片大小为零, 同步进库
        if (dvPlaceNew.getImageWidth() == 0 || dvPlaceNew.getImageHeight() == 0) {
            try {
                BufferedImage image = ImageIO.read(newImage);
                dvPlaceNew.setImageWidth(image.getWidth());
                dvPlaceNew.setImageHeight(image.getHeight());
                dvPlaceService.update(dvPlaceNew);

                // 图片大小更新后，更新内存中 dvPlace 的值
                dvPlace.setImageWidth(image.getWidth());
                dvPlace.setImageHeight(image.getHeight());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
