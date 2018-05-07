package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.OpticsDVPlaceBean;
import com.microwise.proxima.dao.OpticsDVPlaceDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 光学摄像机 dao 实现
 *
 * @author gaohui
 * @date 2012-7-10
 * @check zhang.licong 2012-07-14
 */
@Repository
@Scope("prototype")
public class OpticsDVPlaceDaoImpl extends BaseDaoImpl<OpticsDVPlaceBean>
        implements OpticsDVPlaceDao {

    public OpticsDVPlaceDaoImpl() {
        super(OpticsDVPlaceBean.class);
    }

    @Override
    public List<OpticsDVPlaceBean> findAllEnable() {
        Session session = super.getSession();

        Query query = session
                .createQuery("select dvPlace from OpticsDVPlaceBean dvPlace where dvPlace.enable = true");
        return (List<OpticsDVPlaceBean>) query.list();
    }

    @Override
    public Long findAllEnableCount() {
        Session session = super.getSession();
        String hql = "select count(dvPlace.id) from OpticsDVPlaceBean  dvPlace where dvPlace.enable = true";
        Query query = session.createQuery(hql);
        return (Long) query.uniqueResult();
    }

    @Override
    public List<OpticsDVPlaceBean> findEnableByPage(int pageNumber, int pageSize) {
        Session session = super.getSession();
        String hql = "select dvPlace from OpticsDVPlaceBean dvPlace where dvPlace.enable = true";
        Query query = session.createQuery(hql);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return (List<OpticsDVPlaceBean>) query.list();
    }

    @Override
    public List<OpticsDVPlaceBean> findByMonitorPointId(int monitorPointId) {
        Session session = getSession();
        String hql = "select dvPlace from OpticsDVPlaceBean dvPlace  where monitorPointId = :monitorPointId";
        Query query = session.createQuery(hql);
        query.setParameter("monitorPointId", monitorPointId);
        return (List<OpticsDVPlaceBean>) query.list();
    }

    @Override
    public List<OpticsDVPlaceBean> findByIO(String ioHost, int ioPort) {
        Session session = getSession();
        String hql = "select dvPlace from OpticsDVPlaceBean dvPlace  where ioIp = :ioHost and ioPort = :ioPort";
        Query query = session.createQuery(hql);
        query.setParameter("ioHost", ioHost).setParameter("ioPort", ioPort);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return query.list();
    }

}
