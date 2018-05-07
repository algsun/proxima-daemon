/**
 *
 */
package com.microwise.proxima.service.impl;

import com.microwise.proxima.bean.InfraredDVPlaceBean;
import com.microwise.proxima.dao.InfraredDVPlaceDao;
import com.microwise.proxima.service.InfraredDVPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <pre>
 * 红外热像仪服务层实现
 * </pre>
 *
 * @author zhangpeng
 * @date 2012-7-9
 * @check zhang.licong 2012-07-14
 */
@Service
@Scope("prototype")
@Transactional
public class InfraredDVPlaceServiceImpl implements InfraredDVPlaceService {

    @Autowired
    private InfraredDVPlaceDao infraredDVPlaceDao;

    @Override
    public int save(InfraredDVPlaceBean infraredDVPlace) {
        return (Integer) infraredDVPlaceDao.save(infraredDVPlace);
    }

    @Override
    public InfraredDVPlaceBean findById(int id) {
        return infraredDVPlaceDao.findById(id);
    }

    @Override
    public void update(InfraredDVPlaceBean infraredDVPlace) {
        infraredDVPlaceDao.update(infraredDVPlace);
    }

    @Override
    public List<InfraredDVPlaceBean> findAllEnable() {
        return infraredDVPlaceDao.findAllEnable(true);
    }

    @Override
    public List<InfraredDVPlaceBean> findByMonitorPointId(int monitorPointId) {
        return infraredDVPlaceDao.findByMonitorPointId(monitorPointId);
    }

    @Override
    public List<InfraredDVPlaceBean> findAll() {

        return infraredDVPlaceDao.findAll();

    }

}
