package com.microwise.proxima.util;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author gaohui
 * @date 2012-7-11
 */
public class TimeUtil {
	private static Logger logger = Logger.getLogger(TimeUtil.class);
	private TimeUtil() {
	}

	/**
	 * 此方法用于将时间，转换为相对时间(及和当前时间比过去了多久   如：1分钟前 )。主要用于在界面显示。
	 * @param time
	 * @return
	 */
	public static String sofar(Date time) {
		DateTime dateTime = DateTime.now().withMillis(time.getTime());

		DateTime now = DateTime.now();
		// 如果是同一年
		if (now.getYear() == dateTime.getYear()) {
			// 如果是同一个月
			if (now.getMonthOfYear() == dateTime.getMonthOfYear()) {
				int hours = Hours.hoursBetween(dateTime, now).getHours();
				// 如果相差小于一个小时
				if (hours <= 1) {
					int minuts = Minutes.minutesBetween(dateTime, now)
							.getMinutes();
					if (minuts <= 1) {
						int seconds = Seconds.secondsBetween(dateTime, now)
								.getSeconds();
						if (seconds < 60) {
							seconds = Math.max(seconds, 1);
							return seconds + "秒前";
						}
					}
					if (minuts < 60) {
						return minuts + "分钟前";
					}
				}
				if (now.getDayOfMonth() == dateTime.getDayOfMonth()) {
					return "今天 " + formatToday(time);
				}
			}
			return formatDateHour(time);
		}
		return formatFull(time);
	}

	/**
	 * 转换为年月日时分
	 * 
	 * @param time
	 * @return
	 */
	private static String formatFull(Date time) {
		return DateUtil.formatFull(time);
	}

	/**
	 * 转换为月日时分
	 * 
	 * @param time
	 * @return
	 */
	private static String formatDateHour(Date time) {
		return DateUtil.formatDateHour(time);
	}

	/**
	 * 转换为时分
	 * 
	 * @param time
	 * @return
	 */
	private static String formatToday(Date time) {
		return DateUtil.formatTime(time);
	}

	/**
	 * <p>
	 * 时间转换，从String转Date 
	 * </p>
	 * TOOD 方法名小写 @gaohui 2012-08-27 13:37
	 * 
	 * @author zhang.licong
	 * @date 2012-8-7
	 * @param time
	 *            时间
	 * @param format
	 *            要转换成的时间格式
	 * @return
	 * 
	 * @deprecated change to use DataUtil.  @gaohui 2012-09-19
	 * 
	 */
	public static Date Str2Date(String time, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = df.parse(time);
		} catch (ParseException e) {
			logger.error("String 转 Date 出错：", e);
		}
		return date;
	}
}
