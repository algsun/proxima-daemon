/**
 * 
 */
package com.microwise.proxima.service;

import com.microwise.proxima.bean.DVPlaceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 摄像机点位 service
 * <pre>
 *
 * @author zhangpeng
 * @date 2012-7-10
 */
public interface DVPlaceService {

	public static final Logger log = LoggerFactory.getLogger(InfraredDVPlaceService.class);
	
	/**
	 * 修改摄像机点位
	 * 
	 * @param dvPlace 摄像机点位信息对象
	 *
	 * @author zhangpeng
	 * @date 2012-6-11
	 */
	public void update(DVPlaceBean dvPlace);
	
	/**
	 * 获取摄像机点位
	 * 
	 *
     * @param id 点位的id
     *
     * @author zhangpeng
	 * @date 2012-6-11
	 * 
	 * return DVPlaceBean dv点位信息对象
	 */
	public DVPlaceBean findById(String id);

	/**
	 * 函数功能说明   根据监测点id查询出绑定在监测点上的摄像机的集合
	 * JinGang
	 * 2012-9-5 下午03:03:08
	 * @参数： @param monitorPointId 监测点id
	 * @参数： @return  绑定在监测点上的摄像机的集合
	 */
	public List<DVPlaceBean> findByMonitorPointId(int monitorPointId);


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
