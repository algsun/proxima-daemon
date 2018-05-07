package com.microwise.proxima.dao.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * hibernate base dao
 * @author gaohui
 * @date 2012-5-23
 */
public class HibernateBaseDao {

	protected SessionFactory sessionFactory;

	/**
	 * 初始化 sessionFactory
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	private void initSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 获取session
	 * 
	 * @return
	 */
	protected Session getSession() {
		return currentSession();
	}

	private Session currentSession() {
		//事务必须是开启的，否则获取不到
		return sessionFactory.getCurrentSession();
	}
	

}