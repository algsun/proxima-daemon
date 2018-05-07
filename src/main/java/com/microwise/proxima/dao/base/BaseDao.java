package com.microwise.proxima.dao.base;

import java.io.Serializable;
import java.util.List;

/**
 * 基础 dao
 * 
 * @author gaohui
 * @date 2012-5-24
 */
public interface BaseDao<T> {

	/**
	 * 根据ID查询
	 * 
	 * @param id
	 * @return
	 */
	public abstract T findById(Serializable id);

	/**
	 * 保存
	 * 
	 * @param t
	 * @return
	 */
	public abstract Serializable save(T t);

	/**
	 * 更新
	 * 
	 * @param t
	 */
	public abstract void update(T t);

	/**
	 * 删除
	 * 
	 * @param t
	 */
	public abstract void delete(T t);

	/**
	 * 查询全部
	 * 
	 * @return
	 */
	public List<T> findAll();

}