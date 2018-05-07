package com.microwise.proxima.bean;

import java.io.Serializable;
import java.util.Comparator;


/**
 * @类功能说明：监测点信息实体
 * @公司名称：西安元智系统技术有限责任公司
 * @作者：JinGang
 * @创建时间：2012-9-5 下午01:48:56
 *
 * TODO 由区域替换 zone gaohui 2013-03-18
 */
public class MonitorPointBean {
	
	/**
	 * 监测点比较器, ID 倒序
	 * 
	 * @author gaohui
	 * @date 2012-09-17
	 */
	public static class MonitorPointComparator implements Comparator<MonitorPointBean>,Serializable{
	    /**实现Serializable接口,当比较器被用来构建一个有序的集合,如一个TreeMap，那么TreeMap将是可序列化的  */
        private static final long serialVersionUID = 1L;

        @Override
		public int compare(MonitorPointBean o1, MonitorPointBean o2) {
			return o2.id - o1.id;
		}
	}

	/**
	 * 编号
	 */
	private int id;
	/**
	 * 监测点名称
	 */
	private String pointName;
	/**
	 *文物名称
     * @deprecated
	 */
	private String relicName;

	/**
	 *文物编目_总登记号
     * @deprecated
	 */
	private String registerCode;

	/**
	 *文物ID
     * @deprecated
	 */
	private int relicId;
	
	/**站点ID*/
	private String siteId;
	
	/**
	 *备注
	 */
	private String remark;
	public MonitorPointBean(){}

	public MonitorPointBean(String pointName, int relicId) {
		this.pointName = pointName;
		this.relicId = relicId;
	}

	
	public MonitorPointBean(int id, String pointName, String relicName,
			String registerCode) {
		this.id = id;
		this.pointName = pointName;
		this.relicName = relicName;
		this.registerCode = registerCode;
	}

	public String getRelicName() {
		return relicName;
	}

	public void setRelicName(String relicName) {
		this.relicName = relicName;
	}

	public String getRegisterCode() {
		return registerCode;
	}

	public void setRegisterCode(String registerCode) {
		this.registerCode = registerCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public int getRelicId() {
		return relicId;
	}

	public void setRelicId(int relicId) {
		this.relicId = relicId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    public String getsiteId() {
        return siteId;
    }

    public void setsiteId(String siteId) {
        this.siteId = siteId;
    }
	
	

}
