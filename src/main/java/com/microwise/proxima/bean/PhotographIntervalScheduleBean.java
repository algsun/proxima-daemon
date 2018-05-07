package com.microwise.proxima.bean;

import java.util.Date;

/**
 * 周期拍照(在某个时间范围内，周期执行)
 * 
 * @author gaohui
 * @date 2012-7-31
 */
public class PhotographIntervalScheduleBean extends PhotographScheduleBean {

	/**
	 * 开始时间(精确到分钟)
	 */
	private Date startTime;

	/**
	 * 结束时间(精确到分钟)
	 */
	private Date endTime;

	/**
	 * 间隔时间(多长时间拍照一次，精确到分钟)
	 */
	private int interval;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

}
