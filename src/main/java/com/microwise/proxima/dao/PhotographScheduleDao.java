package com.microwise.proxima.dao;

import com.microwise.proxima.bean.PhotographScheduleBean;
import com.microwise.proxima.dao.base.BaseDao;

import java.util.List;

/**
 * PhotographScheduleDao
 * @author gaohui
 * @date 2012-8-1
 */
public interface PhotographScheduleDao extends BaseDao<PhotographScheduleBean> {

	/**
	 * 查询所有的拍照计划(包括时间点的和周期的)
	 * 
	 * @return
	 */
	public List<PhotographScheduleBean> findAll();

	/**
	 * 查询某个摄像机点位的所有拍照计划
	 * 
	 *
     * @param dvPlaceId
     * @return
	 */
	public List<PhotographScheduleBean> findAllOfDVPlace(String dvPlaceId);
}
