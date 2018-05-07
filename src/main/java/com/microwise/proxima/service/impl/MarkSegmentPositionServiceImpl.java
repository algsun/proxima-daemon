package com.microwise.proxima.service.impl;

import com.microwise.proxima.bean.MarkSegmentPositionBean;
import com.microwise.proxima.beans2.IdentityCode;
import com.microwise.proxima.dao.MarkSegmentPositionDao;
import com.microwise.proxima.service.MarkSegmentPositionService;
import com.microwise.proxima.util.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author gaohui
 * @date 13-6-13 14:34
 */
@Beans.Service
@Transactional
public class MarkSegmentPositionServiceImpl implements MarkSegmentPositionService {

    @Autowired
    private MarkSegmentPositionDao markSegmentPositionDao;

    @Override
    public Serializable save(MarkSegmentPositionBean markSegmentPositionBean) {
        return markSegmentPositionDao.save(markSegmentPositionBean);
    }

    @Override
    public MarkSegmentPositionBean findLatestBefore(String markSegmentId, String pictureId) {
        return markSegmentPositionDao.findLatestBefore(markSegmentId, pictureId);
    }

    @Override
    public List<IdentityCode> findIdentityCodes(String pictureId) {
        return markSegmentPositionDao.findIdentityCodes(pictureId);
    }
}
