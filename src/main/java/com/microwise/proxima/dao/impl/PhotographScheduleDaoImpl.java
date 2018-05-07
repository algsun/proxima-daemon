package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.PhotographScheduleBean;
import com.microwise.proxima.dao.PhotographScheduleDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author gaohui
 * @date 2012-8-1
 */
@Repository
@Scope("prototype")
public class PhotographScheduleDaoImpl extends
        BaseDaoImpl<PhotographScheduleBean> implements PhotographScheduleDao {

	public PhotographScheduleDaoImpl() {
		super(PhotographScheduleBean.class);
	}

	@Override
	public List<PhotographScheduleBean> findAll() {
		Query query = super.getSession().createQuery(
				"select ps from PhotographScheduleBean ps");
		return query.list();
	}

	/**
	 * 查询某个摄像机点位的所有拍照计划
	 * 
	 *
     * @param dvPlaceId
     * @return
	 */
	@Override
	public List<PhotographScheduleBean> findAllOfDVPlace(String dvPlaceId) {
		Query query = super
				.getSession()
				.createQuery(
						"select ps from PhotographScheduleBean ps where ps.dvPlace.id = :dvPlaceId");
		query.setParameter("dvPlaceId", dvPlaceId);
		return query.list();
	}
}
