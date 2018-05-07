package com.microwise.proxima.dao.base;

import org.hibernate.Criteria;

import java.io.Serializable;
import java.util.List;

/**
 * 基础 dao 实现
 * 
 * @author gaohui
 * @date 2012-5-24
 */
public class BaseDaoImpl<T> extends HibernateBaseDao implements BaseDao<T> {

	protected Class<T> clazz;

	public BaseDaoImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	public T findById(Serializable id) {
		return (T) getSession().get(clazz, id);
	}

	public Serializable save(T t) {
		return getSession().save(t);
	}

	public void delete(T t) {
		getSession().delete(t);
	}

	public void update(T t) {
		getSession().update(t);
	}
	
	@Override
	public List<T> findAll(){
		Criteria criteria = getSession().createCriteria(clazz);
		return criteria.list();
	}
}
