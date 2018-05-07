package com.microwise.proxima.service.impl;

import com.google.common.base.Strings;
import com.microwise.proxima.bean.*;
import com.microwise.proxima.dao.OpticsDVPlaceDao;
import com.microwise.proxima.dao.PhotographScheduleDao;
import com.microwise.proxima.photograph.PhotographScheduler;
import com.microwise.proxima.service.OpticsDVPlaceService;
import com.microwise.proxima.util.DateUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

/**
 * 光学摄像机点位 service 实现
 * 
 * @author gaohui
 * @date 2012-7-10
 * 
 * @check zhang.licong 2012-07-14
 */
@Service
@Scope("prototype")
@Transactional
public class OpticsDVPlaceServiceImpl implements OpticsDVPlaceService {

	@Autowired
	private OpticsDVPlaceDao opticsDVPlaceDao;

	@Autowired
	private PhotographScheduleDao photographScheduleDao;

	@Autowired(required = false)
	private PhotographScheduler photographScheduler;

	@Override
	public OpticsDVPlaceBean findById(String id) {
		return opticsDVPlaceDao.findById(id);
	}

	@Override
	public void update(OpticsDVPlaceBean opticsDVPlace) {
		opticsDVPlaceDao.update(opticsDVPlace);
	}

	/**
	 * @author zhang.licong
	 * @throws Exception
	 * @update 2012-8-7
	 */
	@Override
	public void save(OpticsDVPlaceBean dvPlace, String everydayPeriod,
			String everydayPoint, String sevendayPeriod, String sevendayPoint,
			String radioType) throws Exception {

		// 保存点位信息
		opticsDVPlaceDao.save(dvPlace);

		refreshPhotographSchecduler(dvPlace, everydayPeriod, everydayPoint,
				sevendayPeriod, sevendayPoint, radioType);
	}

	@Override
	public List<OpticsDVPlaceBean> findAllEnable() {
		return opticsDVPlaceDao.findAllEnable();
	}

	@Override
	public Long findAllEnableCount() {
		return opticsDVPlaceDao.findAllEnableCount();
	}

	@Override
	public List<OpticsDVPlaceBean> findEnableByPage(int pageNumber, int pageSize) {
		return opticsDVPlaceDao.findEnableByPage(pageNumber, pageSize);
	}

    @Override
    public List<OpticsDVPlaceBean> findByIO(String ioHost, int ioPort) {
        return opticsDVPlaceDao.findByIO(ioHost, ioPort);
    }

	@Override
	public void save(OpticsDVPlaceBean dvPlace) {
		opticsDVPlaceDao.save(dvPlace);
	}

	@Override
	public void update(OpticsDVPlaceBean dvPlace, String everydayPeriod,
			String everydayPoint, String sevendayPeriod, String sevendayPoint,
			String radioType) throws Exception {

		// 修改点位信息
		opticsDVPlaceDao.update(dvPlace);

		// 删除拍照计划数据
		List<PhotographScheduleBean> psList = photographScheduleDao
				.findAllOfDVPlace(dvPlace.getId());
		for (PhotographScheduleBean psBean : psList) {
			photographScheduleDao.delete(psBean);
		}

		refreshPhotographSchecduler(dvPlace, everydayPeriod, everydayPoint,
				sevendayPeriod, sevendayPoint, radioType);
	}

	/**
	 * 判断是星期几？
	 * 
	 * @param sevenDayValue
	 *            值
	 * @author zhang.licong
	 * @date 2012-8-9
	 * 
	 * 
	 */
	private DayType judgmentDayOfWeek(String sevenDayValue) {
		int week = Integer.parseInt(sevenDayValue);
		DayType dayType = null;
		switch (week) {
		case 1:
			dayType = DayType.MON;
			break;
		case 2:
			dayType = DayType.TUES;
			break;
		case 3:
			dayType = DayType.WED;
			break;
		case 4:
			dayType = DayType.THURS;
			break;
		case 5:
			dayType = DayType.FRI;
			break;
		case 6:
			dayType = DayType.SAT;
			break;
		case 7:
			dayType = DayType.SUN;
			break;
		}
		return dayType;
	}

	@Override
	public List<OpticsDVPlaceBean> findByMonitorPointId(int monitorPointId) {
		return opticsDVPlaceDao.findByMonitorPointId(monitorPointId);
	}

	/**
	 * 重置拍照计划
	 * 
	 * @param dvPlace
	 * @param everydayPeriod
	 * @param everydayPoint
	 * @param sevendayPeriod
	 * @param sevendayPoint
	 * @param radioType
	 * @throws java.text.ParseException
	 * @throws org.quartz.SchedulerException
	 */
	private void refreshPhotographSchecduler(OpticsDVPlaceBean dvPlace,
			String everydayPeriod, String everydayPoint, String sevendayPeriod,
			String sevendayPoint, String radioType) throws ParseException,
			SchedulerException {

		// 判断是否有外部控制
		if (dvPlace.isIoOn()) {

			// 判断拍照计划选择类型（每天周期、每天时间点、7天周期、7天时间点）
			if (radioType.equals("day")) {// 判断是否是每天周期
				if (!Strings.isNullOrEmpty(everydayPeriod)) {// 判断每天周期是否为空
					PhotographIntervalScheduleBean interval = new PhotographIntervalScheduleBean();
					String[] dayPeriodValue = everydayPeriod.split(",");
					interval.setStartTime(DateUtil.parseTime(dayPeriodValue[0]));
					interval.setEndTime(DateUtil.parseTime(dayPeriodValue[1]));
					interval.setInterval(Integer.parseInt(dayPeriodValue[2]));
					interval.setDvPlace(dvPlace);
					interval.setDayOfWeek(DayType.ALL);

					// 保存到数据库
					photographScheduleDao.save(interval);

					// 设置拍照计划
					if (photographScheduler != null) {

						// 取消拍照计划
						photographScheduler.cancelAllTrigger(dvPlace);

						// 添加拍照计划
						photographScheduler.addTrigger(dvPlace, interval);
					}

				}
			} else if (radioType.equals("point")) {// 判断是否是每天时间点
				if (!Strings.isNullOrEmpty(everydayPoint)) {// 判断每天时间点是否为空
					if (photographScheduler != null) {
						// 取消拍照计划
						photographScheduler.cancelAllTrigger(dvPlace);
					}
					String[] dayPointValue = everydayPoint.split(",");
					for (int i = 0; i < dayPointValue.length; i++) {
						PhotographPointScheduleBean point = new PhotographPointScheduleBean();
						point.setTimePoint(DateUtil.parseTime(dayPointValue[i]));
						point.setDvPlace(dvPlace);
						point.setDayOfWeek(DayType.ALL);

						// 保存到数据库
						photographScheduleDao.save(point);

						// 设置拍照计划
						if (photographScheduler != null) {
							// 添加拍照计划
							photographScheduler.addTrigger(dvPlace, point);
						}
					}

				}
			} else if (radioType.equals("7day")) {// 判断是否是7天周期
				if (!Strings.isNullOrEmpty(sevendayPeriod)) {// 判断7天周期是否为空

					if (photographScheduler != null) {
						// 取消拍照计划
						photographScheduler.cancelAllTrigger(dvPlace);
					}
					// 解析7天周期
					String[] sevenDay = sevendayPeriod.split("&");
					for (int i = 0; i < sevenDay.length; i++) {
						PhotographIntervalScheduleBean interval = new PhotographIntervalScheduleBean();
						String[] sevenDayValue = sevenDay[i].split(",");

						// 判断是星期几
						interval.setDayOfWeek(judgmentDayOfWeek(sevenDayValue[0]));
						interval.setStartTime(DateUtil
								.parseTime(sevenDayValue[1]));
						interval.setEndTime(DateUtil
								.parseTime(sevenDayValue[2]));
						interval.setInterval(Integer.parseInt(sevenDayValue[3]));
						interval.setDvPlace(dvPlace);

						// 保存到数据库
						photographScheduleDao.save(interval);

						// 设置拍照计划
						if (photographScheduler != null) {
							// 添加拍照计划
							photographScheduler.addTrigger(dvPlace, interval);
						}
					}
				}
			} else if (radioType.equals("7day_point")) {// 判断是否是7天时间点
				if (!Strings.isNullOrEmpty(sevendayPoint)) {// 判断7天时间点是否为空
					if (photographScheduler != null) {
						// 取消拍照计划
						photographScheduler.cancelAllTrigger(dvPlace);
					}
					// 解析7天周期
					String[] sevenDayPoint = sevendayPoint.split("&");
					for (int i = 0; i < sevenDayPoint.length; i++) {
						PhotographPointScheduleBean point = new PhotographPointScheduleBean();
						String[] sevenDayPointValue = sevenDayPoint[i]
								.split(",");

						point.setDayOfWeek(judgmentDayOfWeek(sevenDayPointValue[0]));
						point.setTimePoint(DateUtil
								.parseTime(sevenDayPointValue[1]));
						point.setDvPlace(dvPlace);

						// 保存到数据库
						photographScheduleDao.save(point);

						// 设置拍照计划
						if (photographScheduler != null) {
							// 添加拍照计划
							photographScheduler.addTrigger(dvPlace, point);
						}
					}
				}
			}
		}
	}
}
