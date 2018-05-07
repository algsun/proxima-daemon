package com.microwise.proxima.imagesync.ext;


import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.DVType;
import com.microwise.proxima.bean.PictureBean;

import java.io.File;

/**
 * 图片同步监听器. 如果有新的图片被同步，会显示的调用 onImageSync 方法.
 * <p/>
 * 不需要显示的注册监听器, 只需要实现此接口且同时是一个 spring 的 bean 即可.例如 DefaultImageSyncListener
 * <p/>
 * Date: 12-9-26 Time: 下午8:27
 *
 * @author bastengao
 */
public interface ImageSyncListener {

    /**
     * 每一张图片被同步完成后，会调用此方法
     *
     * @param newImage    新图片
     * @param dvType      图片对应的摄像机点位类型
     * @param dvPlace     摄像机点位
     * @param pictureBean 已经持久的 picture
     */
    public void onImageSync(File newImage, DVType dvType, DVPlaceBean dvPlace, PictureBean pictureBean);
}

