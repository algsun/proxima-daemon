package com.microwise.proxima.dao;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.InfraredMarkRegionBean;
import com.microwise.proxima.dao.base.BaseDao;

import java.util.List;

/**
 * 红外标记区域 dao
 * 
 * @author gaohui
 * @date 2012-9-5
 */
public interface InfraredMarkRegionDao extends BaseDao<InfraredMarkRegionBean> {

	/**
	 * 根据摄像机点位查询红外标记区域
	 * 
	 * @author zhang.licong
	 * @date 2012-9-6
     * @param dvPlaceId
     *
	 */
	public List<InfraredMarkRegionBean> findInfraredMarkRegions(String dvPlaceId);

	/**
	 * 返回摄像机点位下的所有标记区域
	 * 
	 * @param dvPlaceId
	 *            摄像机点位ID
	 * @return
	 */
	public List<InfraredMarkRegionBean> findAllByDVPlaceId(int dvPlaceId);

	/**
	 * 更新标记区域的位置和宽高
	 * 
	 * @param markRegionId
	 *            标记区域ID
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void updateById(int markRegionId, int x, int y, int width, int height);

	/**
	 * 返回标记区域返回对应的摄像机点位
	 * 
	 * @param markRegionId
	 *            标记区域ID
	 * @return
	 */
	public DVPlaceBean findDVPlaceByMarkRegionId(int markRegionId);

	
	/**
	 * 查找某个位置的标记区域
	 * @param dvPlaceId 摄像机点位
	 * @param x 位置
	 * @param y 位置
	 * @return
	 */
	public InfraredMarkRegionBean findAt(int dvPlaceId, int x, int y);
}
