package com.microwise.proxima.service.impl;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.dao.DVPlaceDao;
import com.microwise.proxima.service.DVPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <pre>
 * 摄像机点位信息服务层实现
 * </pre>
 *
 * @author zhangpeng
 * @date 2012-7-10
 */
@Service
@Scope("prototype")
@Transactional
public class DVPlaceServiceImpl implements DVPlaceService {

    @Autowired
    private DVPlaceDao dvPlaceDao;

    @Override
    public void update(DVPlaceBean dvPlace) {
        dvPlaceDao.update(dvPlace);
    }

    @Override
    public DVPlaceBean findById(String id) {
        return dvPlaceDao.findById(id);
    }

    @Override
    public DVPlaceBean findWithFtpById(String dvPlaceId) {
        return dvPlaceDao.findWithFtpById(dvPlaceId);
    }

    @Override
    public List<DVPlaceBean> findByMonitorPointId(int monitorPointId) {
        return this.dvPlaceDao.findByMonitorPointId(monitorPointId);
    }

    @Override
    public List<DVPlaceBean> findAllEnable(int start, int max) {
        return dvPlaceDao.findAllEnable(start, max);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long findEnableCount() {
        return dvPlaceDao.findEnableCount();
    }

    @Override
    public List<DVPlaceBean> findAllEnableByFTP(String ftpProfileId) {
        return dvPlaceDao.findAllEnableByFTP(ftpProfileId);
    }
}
