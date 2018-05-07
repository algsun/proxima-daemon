package com.microwise.proxima.service;

import com.microwise.proxima.bean.OpticsDVPlaceBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 光学摄像机 service
 *
 * @author gaohui
 * @date 2012-7-9
 * @check zhang.licong 2012-07-14
 */
public interface OpticsDVPlaceService {

    /**
     * 更新光学摄像机点位信息
     *
     * @param opticsDVPlace 光学摄像机点位信息对象
     * @author zhangpeng
     * @date 2012-7-11
     * @deprecated
     */
    public void update(OpticsDVPlaceBean opticsDVPlace);

    /**
     * 根据id获取光学摄像机点位信息
     *
     * @param id 光学摄像机点位信息对象id
     * @return InfraredDVPlaceBean 要查询的红外热像仪点位信息对象
     * @author zhangpeng
     * @date 2012-7-11
     */
    public OpticsDVPlaceBean findById(String id);

    /**
     * 查询所有启用的光学摄像机
     *
     * @return List<OpticsDVPlaceBean> 已经启用的光学摄像机列表，注意如果没有返回空集合
     */
    public List<OpticsDVPlaceBean> findAllEnable();

    /**
     * 分页查询所有启用的光学摄像机
     *
     * @param pageNumber 当前页
     * @param pageSize   分页单位
     * @return List<OpticsDVPlaceBean> 已经启用的光学摄像机列表，注意如果没有返回空集合
     */
    public List<OpticsDVPlaceBean> findEnableByPage(int pageNumber, int pageSize);


    /**
     * 根据IO模块IP端口查询摄像机信息
     *
     * @param ioHost IO模块地址
     * @param ioPort IO模块端口号
     * @return 光学摄像机
     */
    public List<OpticsDVPlaceBean> findByIO(String ioHost, int ioPort);

    /**
     * 查询所有启用的光学摄像机 数量
     *
     * @return count  所有已经启用的光学摄像机的数量
     */
    public Long findAllEnableCount();

    /**
     * 保存摄像机点位
     *
     * @param dvPlace 摄像机点位对象
     */
    public void save(OpticsDVPlaceBean dvPlace);

    /**
     * 保存拍照计划和IO模块参数
     *
     * @param dvPlace        摄像机点位对象
     * @param everydayPeriod 每天周期变量
     * @param everydayPoint  每天时间点变量
     * @param sevendayPeriod 7天周期变量
     * @param sevendayPoint  7天时间点变量
     * @author 张俐聪
     * @date 2012-08-07
     */
    public void save(OpticsDVPlaceBean dvPlace, String everydayPeriod,
                     String everydayPoint, String sevendayPeriod, String sevendayPoint,
                     String radioType) throws Exception;

    /**
     * 修改拍照计划和IO模块参数
     *
     * @param dvPlace        摄像机点位对象
     * @param everydayPeriod 每天周期变量
     * @param everydayPoint  每天时间点变量
     * @param sevendayPeriod 7天周期变量
     * @param sevendayPoint  7天时间点变量
     * @author zhang.licong
     * @date 2012-8-9
     */
    public void update(OpticsDVPlaceBean dvPlace, String everydayPeriod,
                       String everydayPoint, String sevendayPeriod, String sevendayPoint,
                       String radioType) throws Exception;

    /**
     * 根据监测点ID查询启用的光学摄像机
     *
     * @param monitorPointId 监测点ID
     * @return 监测点下的所有光学摄像机集合
     */
    public List<OpticsDVPlaceBean> findByMonitorPointId(int monitorPointId);

}
