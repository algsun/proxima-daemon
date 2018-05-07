package com.microwise.proxima.service;

import com.microwise.proxima.bean.PhotographScheduleBean;

import java.util.List;

/**
 * 摄像机拍照计划service接口
 * @author zhang.licong
 * @date 2012-8-8
 */
public interface PhotographScheduleService  {

	/**
	 * 查询所有的拍照计划(包括时间点的和周期的)
	 * 
	 * @return
	 */
	public List<PhotographScheduleBean> findAll();

	/**
	 * 查询某个摄像机点位的所有拍照计划
	 *
     * @param dvPlaceId
     * @return
	 */
	public List<PhotographScheduleBean> findAllOfDVPlace(String dvPlaceId);
}
