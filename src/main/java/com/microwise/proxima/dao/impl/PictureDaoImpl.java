package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.dao.PictureDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片数据库访问层实现
 *
 * @author zhang.licong
 * @date 2012-7-6
 */
@Repository
@Scope("prototype")
public class PictureDaoImpl extends BaseDaoImpl<PictureBean> implements
        PictureDao {

    public PictureDaoImpl() {
        super(PictureBean.class);
    }

    @Override
    public List<PictureBean> findByDVPlace(int dvPlaceId, int start, int max) {
        Session session = super.getSession();

        Query query = session
                .createQuery("select picture from PictureBean picture where picture.dv.id = :dvPlaceId order by picture.saveTime DESC");

        query.setParameter("dvPlaceId", dvPlaceId);
        query.setFirstResult(start);
        query.setMaxResults(max);
        return query.list();
    }

    @Override
    public List<PictureBean> findByDVPlaceWithDuration(int dvPlaceId,
                                                       Date startDate, Date endDate) {
        Session session = super.getSession();

        Query query = session
                .createQuery("select pic from PictureBean pic where pic.dv.id = :dvPlaceId and pic.saveTime >= :startDate and pic.saveTime <= :endDate order by pic.saveTime DESC");
        query.setParameter("dvPlaceId", dvPlaceId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.list();
    }

    @Override
    public DVPlaceBean findDVPlaceByPictureId(String picId) {
        Session session = super.getSession();

        Query query = session.createQuery("select dvPlace from PictureBean pic join pic.dv as dvPlace where pic.id = :picId ");
        query.setParameter("picId", picId);
        query.setMaxResults(1);
        return (DVPlaceBean) query.uniqueResult();
    }

    @Override
    public List<PictureBean> findPictures(String dvPlaceId) {
        Query query = super.getSession()
                .createQuery("select picture from PictureBean picture where picture.dv.id = :dvPlaceId order by picture.saveTime DESC");

        query.setParameter("dvPlaceId", dvPlaceId);
        return query.list();
    }

    @Override
    public PictureBean findNewPictures(String dvPlaceId) {
        Query query = super.getSession()
                .createQuery("select picture from PictureBean picture where picture.dv.id = :dvPlaceId order by picture.saveTime DESC");
        query.setFirstResult(0);
        query.setMaxResults(1);
        query.setParameter("dvPlaceId", dvPlaceId);
        return (PictureBean)query.uniqueResult();

    }


    @Override
    public List<PictureBean> findPicturesAfter(int dvPlaceId, Date dateAfter, int max) {
        Query query = super.getSession().createQuery("select picture from PictureBean picture where picture.dv.id = :dvPlaceId and picture.saveTime <= :dateAfter ORDER BY picture.saveTime DESC");

        query.setParameter("dvPlaceId", dvPlaceId);
        query.setParameter("dateAfter", dateAfter);
        query.setMaxResults(max);

        return query.list();
    }

    @Override
    public Map<String, Object> findLastPhotoTime(List<? extends DVPlaceBean> dvPlaceBeanList) {
        Map<String, Object> lastPhotos = new HashMap<String, Object>();
        for (DVPlaceBean dvPlaceBean : dvPlaceBeanList) {
            Query query = super.getSession().createQuery("select MAX(picture.saveTime) from PictureBean picture where picture.dv.id = :dvPlaceId");
            query.setParameter("dvPlaceId", dvPlaceBean.getId());
            lastPhotos.put(dvPlaceBean.getId(), query.uniqueResult());
        }
        return lastPhotos;
    }
}
