package com.microwise.proxima.bean;

/**
 * 
 * 光学摄像机点位
 * 
 * @author gaohui
 * @date 2012-7-6
 */
public class OpticsDVPlaceBean extends DVPlaceBean {

	/**
	 * 摄像机http访问端口，默认为80
	 * 
	 * @deprecated
	 */
	private int dvHttpPort;

	/**
	 * 抓图时间间隔，单位为分钟
	 * 
	 * @deprecated
	 */
	private int capturePeriod;

	/**
	 * 一次抓图的数量，默认为1
	 * 
	 * @deprecated
	 */
	private int attatchedFileNumber;

	/**
	 * ftp 是否启用
	 * 
	 * @deprecated
	 */
	private boolean ftpEnable;

	/**
	 * 远程摄像机管理界面访问 端口
	 */
	private int dvPort;

	/**
	 * IO模块版本
	 */
	private int ioVersion;

	/**
	 * 远程IO模块IP
	 */
	private String ioIp;

	/**
	 * 远程IO模块端口
	 */
	private int ioPort;

	/**
	 * 拍照前开灯时间 (单位毫秒)
	 */
	private int lightOnTime;

	/**
	 * 拍照后开灯时间(单位毫秒)
	 */
	private int lightOffTime;

	/**
	 * 拍照时间
	 */
	private int photographTime;

	/**
	 * 是否外部控制
	 */
	private boolean ioOn;

	/**
	 * 是否开灯
	 */
	private boolean lightOn;

	/** 灯路数 */
	private Integer ioLightRoute;

	/** 摄像机路数 */
	private Integer ioDvRoute;

	/**
	 * TODO
	 * 
	 * @deprecated 2012-08-17 @gaohui
	 */
	public int getDvHttpPort() {
		return dvHttpPort;
	}

	/**
	 * TODO
	 * 
	 * @param dvHttpPort
	 * @deprecated 2012-08-17 @gaohui
	 */
	public void setDvHttpPort(int dvHttpPort) {
		this.dvHttpPort = dvHttpPort;
	}

	/**
	 * TODO
	 * 
	 * @deprecated 2012-08-17 @gaohui
	 */
	public int getCapturePeriod() {
		return capturePeriod;
	}

	/**
	 * TODO
	 * 
	 * @param capturePeriod
	 * @deprecated 2012-08-17 @gaohui
	 */
	public void setCapturePeriod(int capturePeriod) {
		this.capturePeriod = capturePeriod;
	}

	/**
	 * TODO
	 * 
	 * @deprecated 2012-08-17 @gaohui
	 */
	public int getAttatchedFileNumber() {
		return attatchedFileNumber;
	}

	/**
	 * TODO
	 * 
	 * @param attatchedFileNumber
	 * @deprecated 2012-08-17 @gaohui
	 */
	public void setAttatchedFileNumber(int attatchedFileNumber) {
		this.attatchedFileNumber = attatchedFileNumber;
	}

	/**
	 * TODO
	 * 
	 * @deprecated 2012-08-17 @gaohui
	 */
	public boolean isFtpEnable() {
		return ftpEnable;
	}

	/**
	 * TODO
	 * 
	 * @param ftpEnable
	 * @deprecated 2012-08-17 @gaohui
	 */
	public void setFtpEnable(boolean ftpEnable) {
		this.ftpEnable = ftpEnable;
	}

	public int getIoVersion() {
		return ioVersion;
	}

	public void setIoVersion(int ioVersion) {
		this.ioVersion = ioVersion;
	}

	public String getIoIp() {
		return ioIp;
	}

	public void setIoIp(String ioIp) {
		this.ioIp = ioIp;
	}

	public int getIoPort() {
		return ioPort;
	}

	public void setIoPort(int ioPort) {
		this.ioPort = ioPort;
	}

	public int getLightOnTime() {
		return lightOnTime;
	}

	public void setLightOnTime(int lightOnTime) {
		this.lightOnTime = lightOnTime;
	}

	public int getLightOffTime() {
		return lightOffTime;
	}

	public void setLightOffTime(int lightOffTime) {
		this.lightOffTime = lightOffTime;
	}

	public int getPhotographTime() {
		return photographTime;
	}

	public void setPhotographTime(int photographTime) {
		this.photographTime = photographTime;
	}

	public int getDvPort() {
		return dvPort;
	}

	public void setDvPort(int dvPort) {
		this.dvPort = dvPort;
	}

	public boolean isIoOn() {
		return ioOn;
	}

	public void setIoOn(boolean ioOn) {
		this.ioOn = ioOn;
	}

	public boolean isLightOn() {
		return lightOn;
	}

	public void setLightOn(boolean lightOn) {
		this.lightOn = lightOn;
	}

	public Integer getIoLightRoute() {
		return ioLightRoute;
	}

	public void setIoLightRoute(Integer ioLightRoute) {
		this.ioLightRoute = ioLightRoute;
	}

	public Integer getIoDvRoute() {
		return ioDvRoute;
	}

	public void setIoDvRoute(Integer ioDvRoute) {
		this.ioDvRoute = ioDvRoute;
	}

}
