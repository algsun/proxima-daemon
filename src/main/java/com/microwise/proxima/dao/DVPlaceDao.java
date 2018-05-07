package com.microwise.proxima.dao;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.dao.base.BaseDao;

import java.util.List;

/**
 * 摄像机点位 dao
 * 
 * @author gaohui
 * @date 2012-7-6
 */
public interface DVPlaceDao extends BaseDao<DVPlaceBean> {

	/**
	 * 根据IO模块发送的信息修改摄像机的IP地址
	 * 
	 * @param dvPlaceId
	 *            点位ID
	 * @param dvIp
	 *            摄像机IP
	 * @author zhang.licong
	 * @date 2012-8-15
	 */
	public void updateDvIP(int dvPlaceId, String dvIp);

	/**
	 * @Title: findByMonitorPointId
	 * @Description: 函数功能说明 根据监测点id查询出绑定在监测点上的摄像机的集合
	 * @author JinGang
	 * @date 2012-9-14 下午01:25:07
	 * @param monitorPointId
	 * @return List<DVPlaceBean> 返回类型
	 */
	public List<DVPlaceBean> findByMonitorPointId(int monitorPointId);

	/**
	 * 查询出所有摄像机点位 GuoTian 2012-9-11 下午16:13:08
	 * 
	 * @参数： @param 无
	 * @参数： @return 摄像机点位信息
	 */
	public List<DVPlaceBean> findAllDVPlace();

	/**
	 * 根据摄像机点位名称查询摄像机点位
	 * 
	 * @param dvPlaceName
	 *            摄像机点位名称
	 * @return
	 */
	public DVPlaceBean findByName(String dvPlaceName);

    /**
     * 查询所有启用的摄像机点位
     *
     * @author gaohui
     * @param start
     * @param max
     * @return
     */
    public List<DVPlaceBean> findAllEnable(int start, int max);

    /**
     * 查询所有启用的摄像机数目
     *
     * @author gaohui
     * @return
     */
    public long findEnableCount();

    /**
     * 根据 ftp id 查询所有启用的摄像机
     *
     * @param ftpProfileId
     * @return
     */
    List<DVPlaceBean> findAllEnableByFTP(String ftpProfileId);

    /**
     * 根据ID 查询摄像机并携带 ftp
     * @param dvPlaceId
     * @return
     */
    DVPlaceBean findWithFtpById(String dvPlaceId);
}
