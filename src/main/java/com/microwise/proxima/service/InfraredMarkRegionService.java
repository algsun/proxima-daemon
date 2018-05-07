package com.microwise.proxima.service;

import com.microwise.proxima.bean.InfraredMarkRegionBean;

import java.util.List;

/**
 * 红外标记区域 service
 * 
 * @author gaohui
 * @date 2012-9-5
 */
public interface InfraredMarkRegionService {

	/**
	 * 保存
	 * 
	 * @param markRegion
	 *            新的标记区域
	 * @return 标记区域ID
	 */
	public int save(InfraredMarkRegionBean markRegion);

	/**
	 * 查询所有标记区域
	 * 
	 * @return 所有标记区域列表
	 * @author li.jianfei
	 * @date 2012-09-10
	 */
	public List<InfraredMarkRegionBean> findAll();

	/**
	 * 根据摄像机点位查询红外标记区域
	 * 
	 * @author zhang.licong
	 * @date 2012-9-6
     * @param dvPlaceId
     *
	 */
	public List<InfraredMarkRegionBean> findInfraredMarkRegions(String dvPlaceId);

}
