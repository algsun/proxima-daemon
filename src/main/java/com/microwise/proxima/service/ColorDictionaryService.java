package com.microwise.proxima.service;

import com.microwise.proxima.bean.ColorDictionaryBean;

import java.util.List;

/**
 * <pre>
 * 色轮字典信息服务层
 * </pre>
 *
 * @author li.jianfei
 * @date 2012-09-03
 */

public interface ColorDictionaryService {
	
	/**
	 * 添加色轮信息
	 * 
	 * @param colorDictionary 色轮信息对象
	 *
	 * @author li.jianfei
	 * @date 2012-09-03
	 * 
	 * @return 添加的色轮信息的ID
	 */
	public int save(ColorDictionaryBean colorDictionary);
	
	public ColorDictionaryBean findById(int id);
	
	/**
	 * 获取所有色轮字典表数据
	 * 
	 * @return 色轮数据集合
	 * @author li.jianfei
	 * @date 2012-09-04
	 */
	public List<ColorDictionaryBean> findAll();

}
