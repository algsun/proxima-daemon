package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.dao.DVPlaceDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gaohui
 * @date 2012-7-6
 */
@Repository
@Scope("prototype")
public class DVPlaceDaoImpl extends BaseDaoImpl<DVPlaceBean> implements
        DVPlaceDao {

    public DVPlaceDaoImpl() {
        super(DVPlaceBean.class);
    }

    @Override
    public DVPlaceBean findWithFtpById(String dvPlaceId){
        Query query = super.getSession().createQuery("select dvPlace from DVPlaceBean dvPlace inner join fetch dvPlace.ftp where dvPlace.id = :dvPlaceId");
        query.setParameter("dvPlaceId", dvPlaceId);
        return (DVPlaceBean) query.uniqueResult();
    }

    @Override
    public void updateDvIP(int dvPlaceId, String dvIp) {
        String hql = "update DVPlaceBean set dvIp = :dvIp where id = :dvPlaceId";
        Query query = super.getSession().createQuery(hql);
        query.setParameter("dvIp", dvIp);
        query.setParameter("dvPlaceId", dvPlaceId);
        query.executeUpdate();

    }

    /**
     * 函数功能说明 根据监测点id查询出绑定在监测点上的摄像机的集合
     *
     * @author JinGang
     * @date 2012-9-14 下午01:25:07
     * @参数： @param monitorPointId 监测点id
     * @参数： @return 绑定在监测点上的摄像机的集合
     */
    @Override
    public List<DVPlaceBean> findByMonitorPointId(int monitorPointId) {
        String hql = "select dvPlace from DVPlaceBean dvPlace where monitorPointId = :monitorPointId ORDER BY dvPlace.createTime DESC";
        Query query = super.getSession().createQuery(hql);
        query.setParameter("monitorPointId", monitorPointId);
        // int count= Integer.parseInt(query.uniqueResult().toString()) ;
        return query.list();
    }

    /**
     * 查询出所有摄像机点位
     *
     * @author GuoTian
     * @date 2012-9-11 下午16:13:08
     * @参数： @param 无
     * @参数： @return 摄像机点位信息
     */
    @Override
    public List<DVPlaceBean> findAllDVPlace() {
        Query query = getSession()
                .createQuery(
                        "select dvPlace from DVPlaceBean dvPlace INNER JOIN fetch dvPlace.zone where dvtype = 1 ORDER BY dvPlace.createTime DESC");
        return query.list();
    }

    @Override
    public DVPlaceBean findByName(String dvPlaceName) {
        Query query = super
                .getSession()
                .createQuery(
                        "select dvPlace from DVPlaceBean dvPlace where dvPlace.placeName = :dvPlaceName");
        query.setParameter("dvPlaceName", dvPlaceName);
        query.setMaxResults(1);
        return (DVPlaceBean) query.uniqueResult();
    }

    @Override
    public List<DVPlaceBean> findAllEnable(int start, int max) {
        Query query = super.getSession().createQuery("select dvPlace from DVPlaceBean as dvPlace inner join fetch dvPlace.ftp  where dvPlace.enable = true");

        query.setFirstResult(start);
        query.setMaxResults(max);
        return query.list();
    }

    @Override
    public long findEnableCount() {
        Query query = super.getSession().createQuery("select count(dvPlace.id) from DVPlaceBean dvPlace where dvPlace.enable = true");
        return (Long) query.uniqueResult();
    }

    @Override
    public List<DVPlaceBean> findAllEnableByFTP(String ftpProfileId) {
        Query query = super.getSession().createQuery("select dvPlace from DVPlaceBean dvPlace inner join fetch dvPlace.ftp as ftp where dvPlace.enable = true and ftp.id = :ftpProfileId");
        query.setParameter("ftpProfileId", ftpProfileId);
        return query.list();
    }
}
