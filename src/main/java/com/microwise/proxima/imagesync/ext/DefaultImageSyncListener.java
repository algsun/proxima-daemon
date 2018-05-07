package com.microwise.proxima.imagesync.ext;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.DVType;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.util.Beans;

import java.io.File;

@Beans.Action
public class DefaultImageSyncListener implements ImageSyncListener {
    @Override
    public void onImageSync(File newImage, DVType dvType, DVPlaceBean dvPlace, PictureBean pictureBean) {
        // 这是实现
    }
}