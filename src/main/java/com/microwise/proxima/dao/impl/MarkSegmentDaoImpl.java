package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.MarkSegmentBean;
import com.microwise.proxima.dao.MarkSegmentDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import com.microwise.proxima.util.Beans;
import org.hibernate.Query;

/**
 * @author gaohui
 * @date 13-6-13 14:18
 */
@Beans.Dao
public class MarkSegmentDaoImpl extends BaseDaoImpl<MarkSegmentBean> implements MarkSegmentDao {
    public MarkSegmentDaoImpl() {
        super(MarkSegmentBean.class);
    }

    @Override
    public MarkSegmentBean findByName(String dvPlaceId, String name) {
        Query query = getSession().createQuery("SELECT ms FROM MarkSegmentBean ms WHERE ms.dvPlace.id = :dvPlaceId AND ms.name = :name");
        query.setParameter("dvPlaceId", dvPlaceId);
        query.setParameter("name", name);
        query.setMaxResults(1);
        return (MarkSegmentBean) query.uniqueResult();
    }
}
