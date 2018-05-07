package com.microwise.proxima.dao;

import com.microwise.proxima.bean.OpticsDVPlaceBean;
import com.microwise.proxima.dao.base.BaseDao;

import java.util.List;

/**
 * 光学摄像机 dao
 *
 * @author gaohui
 * @date 2012-7-10
 * @check zhang.licong 2012-07-14
 */
public interface OpticsDVPlaceDao extends BaseDao<OpticsDVPlaceBean> {

    /**
     * 查询所有启用的光学摄像机. 注意如果没有返回空集合
     *
     * @return
     */
    public List<OpticsDVPlaceBean> findAllEnable();
    
	/**
	 * 分页查询所有启用的光学摄像机
	 * 
	 * @param pageNumber
	 *            当前页
	 * @param pageSize
	 *            分页单位
	 * @return List<OpticsDVPlaceBean> 已经启用的光学摄像机列表，注意如果没有返回空集合
	 */
	public List<OpticsDVPlaceBean> findEnableByPage(int pageNumber, int pageSize);
	
	/**
	 * 查询所有启用的光学摄像机 数量
	 * @return  count  所有已经启用的光学摄像机的数量
	 */
	public Long findAllEnableCount();


    /**
     * 根据监测点ID查询启用的光学摄像机
     *
     * @return
     * @author JinGang
     * @date 2012-9-13
     */
    public List<OpticsDVPlaceBean> findByMonitorPointId(int monitorPointId);

	/**
	 * 根据IO模块IP端口查询摄像机信息
	 *
	 * @param ioHost IO模块地址
	 * @param ioPort IO模块端口号
	 * @return 光学摄像机
	 */
	List<OpticsDVPlaceBean> findByIO(String ioHost, int ioPort);
}
