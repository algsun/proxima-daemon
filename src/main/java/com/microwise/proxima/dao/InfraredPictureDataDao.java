package com.microwise.proxima.dao;

import com.microwise.proxima.bean.InfraredPictureDataBean;
import com.microwise.proxima.dao.base.BaseDao;

import java.util.Date;
import java.util.List;

public interface InfraredPictureDataDao extends
		BaseDao<InfraredPictureDataBean> {

	/**
	 * 根据图片ID获取红外图片高低温数据
	 * 
	 *
     * @param picId
     *            红外图像ID
     * @return 红外图片数据对象
	 * @author li.jianfei
	 * @date 2012-09-04
	 */
	public InfraredPictureDataBean findByPicId(String picId);

	/**
	 * 根据指定点位 ID 和起始日期查询所有图片最高温的最大值
	 * 
	 * @param dvPlaceId
	 *            红外摄像机点位 ID
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 查询到的最大值
	 * @author li.jianfei
	 * @date 2012-09-11
	 */
	public double findMaxHighTemperature(int dvPlaceId, Date startDate,
                                         Date endDate);

	/**
	 * 根据指定点位 ID 和起始日期查询所有图片最低温的最小值
	 * 
	 * @param dvPlaceId
	 *            红外摄像机点位 ID
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 查询到的最大值
	 * @author li.jianfei
	 * @date 2012-09-11
	 */
	public double findMinLowTemperature(int dvPlaceId, Date startDate,
                                        Date endDate);

	/**
	 * 根据指定点位 ID 和起始日期查询区域数据集合
	 * 
	 * @param dvPlaceId
	 *            红外摄像机点位 ID
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 红外图片标记区域集合
	 * @author li.jianfei
	 * @date 2012-09-11
	 */
	public List<InfraredPictureDataBean> findListForChart(int dvPlaceId,
                                                          Date startDate, Date endDate);
}
