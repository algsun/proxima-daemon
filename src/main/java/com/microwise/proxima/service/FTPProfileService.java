package com.microwise.proxima.service;

import com.microwise.proxima.bean.FTPProfile;

import java.util.List;
import java.util.Map;

/**
 * ftp配置Service
 * 
 * @author wnagyunlong
 * @date 2013-3-26
 */
public interface FTPProfileService {

	/**
	 * <pre>
	 * 添加ftp配置
	 * 
	 * @param ftp ftp配置对象
	 * 
	 * @return void
	 * </pre>
	 */
	public void save(FTPProfile ftp);

	/**
	 * <pre>
	 * 更新ftp配置对象
	 * 
	 * @param ftp ftp配置对象
	 * 
	 * @return void
	 * </pre>
	 */
	public void update(FTPProfile ftp);

	/**
	 * <pre>
	 * 查询单个ftp配置对象
	 * 
	 * @param ftpId ftp配置对象
	 * 
	 * @return FTPProfile ftp配置对象
	 * </pre>
	 */
	public FTPProfile findById(String ftpId);

	/**
     * <pre>
     * 查询所有ftp服务器配置对象
     *
     * @return List<FTPProfile> ftp配置对象列表
     * </pre>
     */
    public List<FTPProfile> findAllFtp();

	/**
	 * <pre>
	 * 判断ftp服务器是否畅通
	 * 
	 * @param ftp ftp配置对象
	 * 
	 * @return String 
	 * 当返回值为null时，表示通畅
	 * 当返回值为具体字符串时，表示异常
	 * </pre>
	 */
	public Map<String ,Object> connectFtp(FTPProfile ftp);

}
