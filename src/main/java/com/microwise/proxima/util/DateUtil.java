package com.microwise.proxima.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间，日期工具类
 * 
 * @author gaohui
 * @date 2012-9-19
 */
public class DateUtil {
	
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String MM_DD_HH_MM = "MM-dd HH:mm";
	public static final String HH_MM = "HH:mm";
	
    public static final String FULL_DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_STRING_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_STRING_FORMAT = "MM-dd HH:mm";
    public static final String TIME_STRING_FORMAT = "HH:mm";

	public static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(YYYY_MM_DD);
	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(MM_DD_HH_MM);
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(HH_MM);
    
	/**
	 * 返回 "yyyy-MM-dd HH:mm" 格式字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String formatFull(Date date) {
		return FULL_DATE_FORMAT.format(date);
	}

	/**
	 * 根据 "yyyy-MM-dd HH:mm" 解析, 返回 Date
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseFull(String dateStr) {
		return parseUncheckException(FULL_DATE_FORMAT, dateStr);
	}

	/**
	 * 转换为时分
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTime(Date time) {
		return TIME_FORMAT.format(time);
	}

	/**
	 * 解析时分. 格式为 "HH:mm"
	 * 
	 * @param timeStr
	 *            时间字符串
	 * @return
	 */
	public static Date parseTime(String timeStr) throws ParseException {
		return parseUncheckException(TIME_FORMAT, timeStr);
	}

	/**
	 * 以指定的方式格式化时间
	 * 
	 * @param date
	 *            时间
	 * @param format
	 *            格式
	 * @return
	 */
	public static String format(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 以指定的方式解析时间
	 * 
	 * @param dateStr
	 *            时间字符串
	 * @param format
	 *            格式
	 */
	public static Date parse(String dateStr, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(dateStr);
		}catch(ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 转换为月日时分(MM-dd HH:mm)
	 * 
	 * @param time
	 * @return
	 */
	public static String formatDateHour(Date time) {
		return DATE_TIME_FORMAT.format(time);
	}
	
	/**
	 * 解析时间，如果有异常, 换为免检异常
	 * 
	 * @param df
	 * @param dateStr
	 * @return
	 * @throw IllegalArgumentException if ParseException
	 */
	public static Date parseUncheckException(DateFormat df, String dateStr) {
		try {
			return df.parse(dateStr);
		}catch(ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
