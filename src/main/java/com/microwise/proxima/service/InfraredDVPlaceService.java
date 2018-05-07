/**
 * 
 */
package com.microwise.proxima.service;

import com.microwise.proxima.bean.InfraredDVPlaceBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <pre>
 * 红外热像仪点位信息服务层
 * </pre>
 * 
 * @author zhangpeng
 * @date 2012-7-9
 * 
 * @check zhang.licong 2012-07-14
 */
@Transactional
public interface InfraredDVPlaceService {

	/**
	 * 添加红外热像仪点位信息
	 * 
	 * @param infraredDVPlace
	 *            红外热像仪点位信息对象
	 * 
	 * @author zhangpeng
	 * @date 2012-7-11
	 * 
	 * @return 添加的红外摄像机的ID
	 */
	public int save(InfraredDVPlaceBean infraredDVPlace);

	/**
	 * 修改红外热像仪点位信息
	 * 
	 * @param infraredDVPlace
	 *            红外热像仪点位信息对象
	 * 
	 * @author zhangpeng
	 * @date 2012-7-11
	 */
	public void update(InfraredDVPlaceBean infraredDVPlace);

	/**
	 * 根据id获取红外热像仪点位信息
	 * 
	 * @param id
	 *            红外热像仪点位信息对象id
	 * 
	 * @author zhangpeng
	 * @date 2012-7-11
	 * 
	 * @return InfraredDVPlaceBean 要查询的红外热像仪点位信息对象
	 */
	public InfraredDVPlaceBean findById(int id);

    /**
	 * 获取所有站点红外热像仪列表
	 * 
	 * @author zhang.licong
	 * @date 2012-10-23
	 * 
	 * @return List<InfraredDVPlaceBean> 获取的红外热像仪的List
	 */
	public List<InfraredDVPlaceBean> findAll();
	
	/**
	 * 获取指定状态的红外热像仪点位信息
	 * 
	 * @author zhangpeng
	 * @date 2012-7-11
	 * @return List<InfraredDVPlaceBean> 封装着指定状态红外热像仪点位信息对象的List
	 */
	public List<InfraredDVPlaceBean> findAllEnable();

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
