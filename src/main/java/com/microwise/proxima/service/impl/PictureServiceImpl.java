package com.microwise.proxima.service.impl;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.dao.PictureDao;
import com.microwise.proxima.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author zhang.licong
 * @date 2012-7-10
 */
@Service
@Scope("prototype")
@Transactional
public class PictureServiceImpl implements PictureService {

    @Autowired
    private PictureDao pictureDao;

    /**
     * 保存摄像机图片
     *
     * @author zhang.licong
     * @date 2012-7-10
     */
    public String savePicture(PictureBean pictureBean) {
        return (String) this.pictureDao.save(pictureBean);

    }

    @Override
    public PictureBean findById(String picId) {
        return pictureDao.findById(picId);
    }

    @Override
    public PictureBean findNewPictures(String dvPlaceId) {
        return pictureDao.findNewPictures(dvPlaceId);
    }

    @Override
    public Map<String, Object> findLastPhotoTime(List<? extends DVPlaceBean> dvPlaceBeanList) {
        return pictureDao.findLastPhotoTime(dvPlaceBeanList);
    }
}
