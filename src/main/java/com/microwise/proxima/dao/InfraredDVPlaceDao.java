package com.microwise.proxima.dao;

import com.microwise.proxima.bean.InfraredDVPlaceBean;
import com.microwise.proxima.dao.base.BaseDao;

import java.util.List;

/**
 * <pre>
 * 红外热像仪点位信息数据库访问层，继承BaseDao
 * </pre>
 * 
 * @author zhangpeng
 * @date 2012-7-9
 * 
 * @check zhang.licong 2012-07-14
 */
public interface InfraredDVPlaceDao extends BaseDao<InfraredDVPlaceBean> {

	/**
	 * 获取开启的红外热像仪
	 * 
	 * @param enable
	 *            开启状态
	 * 
	 * @author zhangpeng
	 * @date 2012-7-11
	 * 
	 * @return 开启的红外热像仪List
	 */
	public List<InfraredDVPlaceBean> findAllEnable(boolean enable);

	/**
	 *<pre>
	 * 返回所有的红外摄像机点位（带有监测点）. 默认按创建时间降序排列
	 * </pre>
	 * @author gaohui
	 * @date 2012-09-06 AM
	 * @return
	 * 
	 * <pre>
	 * 添加站点
     * </pre>
	 * @param siteId 站点Id
	 * @author fenghua
	 * @date 2012-10-22
	 * 
	 */
	public List<InfraredDVPlaceBean> findAllWithMonitorPoint(String siteId);

	/**
	 * 根据监测点ID查询启用的红外摄像机
	 * 
	 * @author JinGang
	 * @date 2012-09-11
	 * @param monitorPointId
	 *            监测点ID
	 * @return 监测点下的所有红外摄像机集合
	 */
	public List<InfraredDVPlaceBean> findByMonitorPointId(int monitorPointId);

}
