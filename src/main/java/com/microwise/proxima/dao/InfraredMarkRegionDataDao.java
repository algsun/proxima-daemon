package com.microwise.proxima.dao;

import com.microwise.proxima.bean.InfraredMarkRegionDataBean;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.dao.base.BaseDao;

import java.util.Date;
import java.util.List;

/**
 * 红外标记区域数据 dao
 * 
 * @author gaohui
 * @date 2012-9-5
 */
public interface InfraredMarkRegionDataDao extends
		BaseDao<InfraredMarkRegionDataBean> {

	/**
	 * 根据红外图片区域查询没有解析的图片
	 * 
	 *
     * @param markRegionId
     *            区域ID
     * @param dvPlaceId
     *            摄像机点位ID
     * @author zhang.licong
	 * @date 2012.9.9
	 * @return
	 */
	public List<PictureBean> findNoResolutionPictures(String markRegionId,
                                                      String dvPlaceId);

	/**
	 * 删除标记区域下的所有标记区域数据
	 * 
	 * @author gao.hui
	 * @date 2012-09-11(今天是个特殊的日子)
	 * 
	 * @param markRegionId
	 *            标记区域ID
	 */
	public void deleteOfMarkRegion(int markRegionId);

	/**
	 * 根据指定点位 ID、区域id 和起始日期查询所有图片区域最高温的最大值
	 * 
	 * @param dvPlaceId
	 *            红外摄像机点位 ID
	 * @param markRegionId
	 *            标记区域id
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 查询到的最大值
	 * @author li.jianfei
	 * @date 2012-09-11
	 */
	public double findMaxHighTemperature(int dvPlaceId, int markRegionId,
                                         Date startDate, Date endDate);

	/**
	 * 根据指定点位 ID 和起始日期查询所有图片最低温的最小值
	 * 
	 * @param dvPlaceId
	 *            红外摄像机点位 ID
	 * @param markRegionId
	 *            标记区域id
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 查询到的最大值
	 * @author li.jianfei
	 * @date 2012-09-11
	 */
	public double findMinLowTemperature(int dvPlaceId, int markRegionId,
                                        Date startDate, Date endDate);

	/**
	 * 根据指定点位 ID 和起始日期查询区域数据集合
	 * 
	 * @param dvPlaceId
	 *            红外摄像机点位 ID
	 * @param markRegionId
	 *            标记区域id
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 红外图片标记区域集合
	 * @author li.jianfei
	 * @date 2012-09-11
	 */
	public List<InfraredMarkRegionDataBean> findListForChart(int dvPlaceId,
                                                             int markRegionId, Date startDate, Date endDate);

	/**
	 * 根据标记区域ID 和 图片ID 返回对应的标记区域数据
	 *
     * @param markRegionId 标记区域ID
     * @param pictureId 图片ID
     * @return
	 */
	public InfraredMarkRegionDataBean findByMakrRegionIdAndPicId(String markRegionId, int pictureId);

}
