package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.InfraredDVPlaceBean;
import com.microwise.proxima.dao.InfraredDVPlaceDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <pre>
 * 红外热像仪点位信息数据库访问层的实现
 * </pre>
 * 
 * @author zhangpeng
 * @date 2012-7-9
 */
@Repository
@Scope("prototype")
@Transactional
public class InfraredDVPlaceDaoImpl extends BaseDaoImpl<InfraredDVPlaceBean>
		implements InfraredDVPlaceDao {

	public InfraredDVPlaceDaoImpl() {
		super(InfraredDVPlaceBean.class);
	}

	@Override
	public List<InfraredDVPlaceBean> findAllEnable(boolean enable) {
		String hql = " select i from InfraredDVPlaceBean i where i.enable= "
				+ enable + " ORDER BY i.createTime desc ";
		Query query = getSession().createQuery(hql);
		List<InfraredDVPlaceBean> infraredDVPlaceList = query.list();
		return infraredDVPlaceList;
	}
	
	@Override
	public List<InfraredDVPlaceBean> findAllWithMonitorPoint(String siteId) {
		Query query = getSession()
				.createQuery(
						"select dvPlace from InfraredDVPlaceBean dvPlace INNER JOIN fetch dvPlace.monitorPoint mon  where mon.siteId=:siteId ORDER BY dvPlace.createTime DESC ");
		query.setParameter("siteId", siteId);
		return query.list();
	}

	@Override
	public List<InfraredDVPlaceBean> findByMonitorPointId(int monitorPointId) {
		Session session = getSession();
		String hql = "select dvPlace from InfraredDVPlaceBean dvPlace  where monitorPointId = :monitorPointId";
		Query query = session.createQuery(hql);
		query.setParameter("monitorPointId", monitorPointId);
		return (List<InfraredDVPlaceBean>) query.list();
	}

}
