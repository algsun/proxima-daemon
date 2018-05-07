package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.MarkSegmentPositionBean;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.beans2.IdentityCode;
import com.microwise.proxima.dao.MarkSegmentPositionDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import com.microwise.proxima.util.Beans;
import org.hibernate.Query;

import java.util.Date;
import java.util.List;

/**
 * @author gaohui
 * @date 13-6-13 14:32
 */
@Beans.Dao
public class MarkSegmentPositionDaoImpl extends BaseDaoImpl<MarkSegmentPositionBean> implements MarkSegmentPositionDao {
    public MarkSegmentPositionDaoImpl() {
        super(MarkSegmentPositionBean.class);
    }

    @Override
    public MarkSegmentPositionBean findLatestBefore(String markSegmentId, String pictureId){

        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT msp from MarkSegmentPositionBean msp ")
                .append("INNER JOIN msp.markSegment as ms ")
                .append("INNER JOIN msp.picture as p ")
                .append("WHERE ms.id = :markSegmentId AND p.saveTime < :saveTime ORDER BY p.saveTime DESC ");

        PictureBean picture = (PictureBean) getSession().get(PictureBean.class, pictureId);
        Date pictureSaveTime = picture.getSaveTime();
        Query query = getSession().createQuery(queryStr.toString());
        query.setParameter("markSegmentId", markSegmentId);
        query.setParameter("saveTime", pictureSaveTime);
        query.setMaxResults(1);

        return (MarkSegmentPositionBean) query.uniqueResult();
    }

    @Override
    public List<IdentityCode> findIdentityCodes(String pictureId) {
        String hql = "SELECT ic from IdentityCode ic left outer join fetch ic.picture where ic.picture.id = :pictureId order by ic.id desc";
        Query query = getSession().createQuery(hql);
        query.setParameter("pictureId",pictureId);
        return query.list();
    }
}
