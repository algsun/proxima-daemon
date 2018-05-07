package com.microwise.proxima.dao;

import com.microwise.proxima.bean.FTPProfile;
import com.microwise.proxima.dao.base.BaseDao;

import java.util.List;

/**
 * ftp配置Dao层
 * 
 * @author Wang yunlong
 * @time 13-3-26 上午10:06
 */
public interface FTPProfileDao extends BaseDao<FTPProfile> {

	/**
	 * <pre>
	 * 判断ftp配置是否正在被使用
	 * 
	 * @param id ftp配置id
	 * 
	 * @return boolean true使用中/false无人使用
	 * </pre>
	 */
	public boolean isUsing(String id);

    /**
     * 查询站点下所有的ftp
     * @param siteId
     * @return
     */
    public List<FTPProfile> findAllBySiteId(String siteId);

}
