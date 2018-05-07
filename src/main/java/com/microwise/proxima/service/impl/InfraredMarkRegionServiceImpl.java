package com.microwise.proxima.service.impl;

import com.microwise.proxima.bean.InfraredMarkRegionBean;
import com.microwise.proxima.dao.InfraredMarkRegionDao;
import com.microwise.proxima.dao.InfraredMarkRegionDataDao;
import com.microwise.proxima.service.InfraredMarkRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * InfraredMarkRegionService 实现
 * 
 * @author gaohui
 * @date 2012-9-10
 */
@Service
@Scope("prototype")
@Transactional
public class InfraredMarkRegionServiceImpl implements InfraredMarkRegionService {

	@Autowired
	private InfraredMarkRegionDao markRegionDao;
	@Autowired
	private InfraredMarkRegionDataDao markRegionDataDao;

	@Override
	public int save(InfraredMarkRegionBean markRegion) {
		return (Integer) markRegionDao.save(markRegion);
	}

	@Override
	public List<InfraredMarkRegionBean> findInfraredMarkRegions(String dvPlaceId) {
		return markRegionDao.findInfraredMarkRegions(dvPlaceId);
	}

	@Override
	public List<InfraredMarkRegionBean> findAll() {
		return markRegionDao.findAll();
	}

}
