package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.FTPProfile;
import com.microwise.proxima.dao.FTPProfileDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ftp配置Dao层实现类
 *
 * @author Wang yunlong
 * @time 13-3-26 上午10:14
 */
@Repository
@Scope("prototype")
@Transactional
public class FTPProfileDaoImpl extends BaseDaoImpl<FTPProfile> implements
        FTPProfileDao {

    public FTPProfileDaoImpl() {
        super(FTPProfile.class);
    }

    @Override
    public boolean isUsing(String id) {
        Query query = super.getSession().createQuery(
                "select count(dv.id) from DVPlaceBean dv where dv.ftp.id=:id");
        query.setParameter("id", id);
        return ((Long)query.uniqueResult()).intValue() > 0;
    }

    @Override
    public List<FTPProfile> findAllBySiteId(String siteId) {
        Query query = super.getSession().createQuery("select ftp from FTPProfile ftp where ftp.site.siteId=:siteId");
        query.setParameter("siteId", siteId);
        return query.list();
    }

}