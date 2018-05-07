package com.microwise.proxima.service;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.PictureBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author zhang.licong
 * @date 2012-7-10
 * 
 */
public interface PictureService {

	public String savePicture(PictureBean pictureBean);

	/**
	 * 根据摄像机点位查询最新图片信息
	 *
	 * @param dvPlaceId 摄像机点位ID
	 *
	 * @author liuzhu
	 * @date 2014-8-18
	 */
	public PictureBean findNewPictures(String dvPlaceId);

	/**
	 * 根据图片ID查询图片
	 *
     * @param picId
     * @return
	 */
	public PictureBean findById(String picId);

	public Map<String,Object> findLastPhotoTime(List<? extends DVPlaceBean> dvPlaceBeanList);
}
