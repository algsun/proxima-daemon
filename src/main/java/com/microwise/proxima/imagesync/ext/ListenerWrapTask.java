package com.microwise.proxima.imagesync.ext;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.DVType;
import com.microwise.proxima.bean.PictureBean;

import java.io.File;

/**
 * 将 ImageSyncListener 进行简单的包装, 让其变成 Runnable
 *
 * @author gaohui
 * @date 13-6-19 17:01
 */
public class ListenerWrapTask implements Runnable {
    private ImageSyncListener imageSyncListener;
    private File newImage;
    private DVType dvType;
    private DVPlaceBean dvPlace;
    private PictureBean picture;

    public ListenerWrapTask(ImageSyncListener imageSyncListener, File newImage, DVType dvType, DVPlaceBean dvPlace, PictureBean picture) {
        this.imageSyncListener = imageSyncListener;
        this.newImage = newImage;
        this.dvType = dvType;
        this.dvPlace = dvPlace;
        this.picture = picture;
    }

    @Override
    public void run() {
        imageSyncListener.onImageSync(newImage, dvType, dvPlace, picture);
    }
}
