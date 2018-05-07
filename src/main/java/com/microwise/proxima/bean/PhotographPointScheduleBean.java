package com.microwise.proxima.bean;

import java.util.Date;

/**
 * 时间点拍照
 * 
 * @author gaohui
 * @date 2012-7-31
 */
public class PhotographPointScheduleBean extends PhotographScheduleBean {

	/**
	 * 拍照时间点(精确到分钟)
	 */
	private Date timePoint;

	public Date getTimePoint() {
		return timePoint;
	}

	public void setTimePoint(Date timePoint) {
		this.timePoint = timePoint;
	}

}
