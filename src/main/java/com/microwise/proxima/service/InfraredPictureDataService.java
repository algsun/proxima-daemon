package com.microwise.proxima.service;

import com.microwise.proxima.bean.InfraredPictureDataBean;

/**
 * <pre>
 * 红外图片数据信息服务层
 * </pre>
 *
 * @author li.jianfei
 * @date 2012-09-03
 */

public interface InfraredPictureDataService {
	
	/**
	 * 添加红外图片数据
	 * 
	 * @param infraredPictureData 红外图片数据对象
	 *
	 * @author li.jianfei
	 * @date 2012-09-03
	 * 
	 * @return 添加的红外图片数据的ID
	 */
	public void save(InfraredPictureDataBean infraredPictureData);
	
	
	/**
	 * 根据ID
	 * @param uuid
	 * @return
	 */
	public InfraredPictureDataBean findById(String uuid);
	
	/**
	 * 根据图片ID获取红外图片高低温数据
	 * 
	 *
     * @param picId 红外图像ID
     * @return 红外图片数据对象
	 * @author li.jianfei
	 * @date 2012-09-04
	 */
	public InfraredPictureDataBean findByPicId(String picId);

}
