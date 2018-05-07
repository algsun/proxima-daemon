package com.microwise.proxima.service.impl;

import com.microwise.proxima.bean.InfraredMarkRegionDataBean;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.dao.InfraredMarkRegionDao;
import com.microwise.proxima.dao.InfraredMarkRegionDataDao;
import com.microwise.proxima.service.InfraredMarkRegionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 红外图片区域数据服务层
 *
 * @author zhang.licong
 * @date 2012-9-6
 */
@Service
@Scope("prototype")
@Transactional
public class InfraedMarkRegionDataServiceImpl implements
        InfraredMarkRegionDataService {

    @Autowired
    private InfraredMarkRegionDataDao infraredMarkRegionDataDao;
    @Autowired
    private InfraredMarkRegionDao markRegionDao;

    @Override
    public void save(InfraredMarkRegionDataBean infraredMarkRegionData) {
        this.infraredMarkRegionDataDao.save(infraredMarkRegionData);

    }

    @Override
    public List<PictureBean> findNoResolutionPictures(String markRegionId,
                                                      String dvPlaceId) {
        return this.infraredMarkRegionDataDao.findNoResolutionPictures(
                markRegionId, dvPlaceId);
    }

}
