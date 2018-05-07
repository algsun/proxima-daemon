package com.microwise.proxima.service;

import com.microwise.proxima.bean.InfraredMarkRegionDataBean;
import com.microwise.proxima.bean.PictureBean;

import java.util.List;

/**
 * 红外标记区域数据 service
 * 
 * @author gaohui
 * @date 2012-9-5
 */
public interface InfraredMarkRegionDataService {

	/**
	 * 保存红外区域数据
	 * 
	 * @author zhang.licong
	 * @date 2012-9-6
	 * 
	 * 
	 */
	public void save(InfraredMarkRegionDataBean infraredMarkRegionData);

	/**
	 * 根据红外图片区域查询没有解析的图片
	 * 
	 *
     * @param markRegionId
     *            区域ID
     * @param dvPlaceId
     *            摄像机点位ID
     * @author zhang.licong
	 * @date 2012.9.9
	 * @return
	 */
	public List<PictureBean> findNoResolutionPictures(String markRegionId,
                                                      String dvPlaceId);

}
