package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.InfraredMarkRegionDataBean;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.dao.InfraredMarkRegionDataDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 红外标记区域数据 dao 实现
 * 
 * @author gaohui
 * @date 2012-9-5
 */
@Repository
@Scope("prototype")
public class InfraredMarkRegionDataDaoImpl extends
        BaseDaoImpl<InfraredMarkRegionDataBean> implements
        InfraredMarkRegionDataDao {
	public InfraredMarkRegionDataDaoImpl() {
		super(InfraredMarkRegionDataBean.class);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<PictureBean> findNoResolutionPictures(String markRegionId,
			String dvPlaceId) {
		Session session = super.getSession();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT {t.*} FROM pictures t LEFT JOIN (SELECT ir.picid FROM infrared_mark_region im,infrared_region_data ir WHERE im.id=ir.markRegionId AND im.id = :markRegionId) a ON (t.id = a.picid) WHERE  ISNULL(a.picid) AND t.dvPlaceId = :dvPlaceId");
		SQLQuery query = session.createSQLQuery(sql.toString()).addEntity("t",
				PictureBean.class);
		query.setParameter("markRegionId", markRegionId);
		query.setParameter("dvPlaceId", dvPlaceId);

		return query.list();
	}

	@Override
	public void deleteOfMarkRegion(int markRegionId) {
		Query query = getSession()
				.createQuery(
						"delete InfraredMarkRegionDataBean d where d.markRegion.id = :markRegionId");
		query.setParameter("markRegionId", markRegionId);

		query.executeUpdate();
	}

	@Override
	public double findMaxHighTemperature(int dvPlaceId, int markRegionId,
			Date startDate, Date endDate) {

		double maxValue = 0.0d;

		Session session = super.getSession();

		Query query = session
				.createQuery("select max(imrd.highTemperature) from InfraredMarkRegionDataBean imrd join imrd.picture p join p.dv d join imrd.markRegion mr where d.id = :dvPlaceId and mr.id= :markRegionId and p.saveTime >= :startDate and p.saveTime <= :endDate");
		query.setParameter("dvPlaceId", dvPlaceId);
		query.setParameter("markRegionId", markRegionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		Object object = query.uniqueResult();
		if (object != null) {
			maxValue = (Double) object;
		}

		return maxValue;
	}

	@Override
	public double findMinLowTemperature(int dvPlaceId, int markRegionId,
			Date startDate, Date endDate) {

		double minValue = 0.0d;

		Session session = super.getSession();

		Query query = session
				.createQuery("select min(imrd.lowTemperature) from InfraredMarkRegionDataBean imrd join imrd.picture p join p.dv d join imrd.markRegion mr where d.id = :dvPlaceId and mr.id= :markRegionId and p.saveTime >= :startDate and p.saveTime <= :endDate");
		query.setParameter("dvPlaceId", dvPlaceId);
		query.setParameter("markRegionId", markRegionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		Object object = query.uniqueResult();
		if (object != null) {
			minValue = (Double) object;
		}
		return minValue;
	}

	@Override
	public List<InfraredMarkRegionDataBean> findListForChart(int dvPlaceId,
			int markRegionId, Date startDate, Date endDate) {
		Session session = super.getSession();

		Query query = session
				.createQuery("select imrd from InfraredMarkRegionDataBean imrd join fetch imrd.picture p join fetch p.dv d join imrd.markRegion mr where d.id = :dvPlaceId and mr.id= :markRegionId and p.saveTime >= :startDate and p.saveTime <= :endDate order by p.saveTime");
		query.setParameter("dvPlaceId", dvPlaceId);
		query.setParameter("markRegionId", markRegionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		return query.list();
	}

	
	@Override
	public InfraredMarkRegionDataBean findByMakrRegionIdAndPicId(String markRegionId, int pictureId) {
		Query query = super.getSession().createQuery("select mrd from InfraredMarkRegionDataBean mrd where mrd.markRegion.id = :markRegionId and mrd.picture.id = :pictureId");
		query.setParameter("markRegionId", markRegionId);
		query.setParameter("pictureId", pictureId);
		query.setMaxResults(1);
		return (InfraredMarkRegionDataBean)query.uniqueResult();
	}
}
