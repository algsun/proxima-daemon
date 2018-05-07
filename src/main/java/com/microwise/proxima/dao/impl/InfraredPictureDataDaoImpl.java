package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.InfraredPictureDataBean;
import com.microwise.proxima.dao.InfraredPictureDataDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 * 红外图片数据的数据库访问层实现
 * </pre>
 * 
 * @author li.jianfei
 * @date 2012-09-03
 */

@Repository
@Scope("prototype")
public class InfraredPictureDataDaoImpl extends
        BaseDaoImpl<InfraredPictureDataBean> implements InfraredPictureDataDao {

	public InfraredPictureDataDaoImpl() {
		super(InfraredPictureDataBean.class);
	}

	@Override
	public InfraredPictureDataBean findByPicId(String picId) {
		Session session = super.getSession();
		Query query = session
				.createQuery("select ipd from InfraredPictureDataBean as ipd where ipd.picture.id = :picId");
		query.setParameter("picId", picId);
		query.setMaxResults(1);
		return (InfraredPictureDataBean) query.uniqueResult();
	}

	@Override
	public double findMaxHighTemperature(int dvPlaceId, Date startDate,
			Date endDate) {

		double maxValue = 0;

		Session session = super.getSession();

		Query query = session
				.createQuery("select max(ipd.highTemperature) from InfraredPictureDataBean ipd join ipd.picture p join p.dv d where d.id = :dvPlaceId and p.saveTime >= :startDate and p.saveTime <= :endDate");
		query.setParameter("dvPlaceId", dvPlaceId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		Object object = query.uniqueResult();
		if (object != null) {
			maxValue=(Double)object;
		}
		return maxValue;
	}

	@Override
	public double findMinLowTemperature(int dvPlaceId, Date startDate,
			Date endDate) {
		
		double minValue=0;

		Session session = super.getSession();

		Query query = session
				.createQuery("select min(ipd.lowTemperature) from InfraredPictureDataBean ipd join ipd.picture p join p.dv d where d.id = :dvPlaceId and p.saveTime >= :startDate and p.saveTime <= :endDate");
		query.setParameter("dvPlaceId", dvPlaceId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		Object object=query.uniqueResult();
		
		if(object!=null){
			minValue=(Double)object;
		}
		return minValue;
	}

	@Override
	public List<InfraredPictureDataBean> findListForChart(int dvPlaceId,
			Date startDate, Date endDate) {

		Session session = super.getSession();

		Query query = session
				.createQuery("select ipd from InfraredPictureDataBean ipd join fetch ipd.picture p join fetch p.dv d where d.id = :dvPlaceId and p.saveTime >= :startDate and p.saveTime <= :endDate");
		query.setParameter("dvPlaceId", dvPlaceId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		return query.list();
	}

}
