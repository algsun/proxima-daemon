package com.microwise.proxima.service.impl;

import com.microwise.proxima.bean.MarkSegmentBean;
import com.microwise.proxima.dao.MarkSegmentDao;
import com.microwise.proxima.service.MarkSegmentService;
import com.microwise.proxima.util.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author gaohui
 * @date 13-6-13 14:20
 */
@Beans.Service
@Transactional
public class MarkSegmentServiceImpl implements MarkSegmentService {

    @Autowired
    private MarkSegmentDao markSegmentDao;

    @Override
    public Serializable save(MarkSegmentBean markSegmentBean) {
        return markSegmentDao.save(markSegmentBean);
    }

    @Override
    public MarkSegmentBean findByName(String dvPlaceId, String name) {
        return markSegmentDao.findByName(dvPlaceId, name);
    }
}
