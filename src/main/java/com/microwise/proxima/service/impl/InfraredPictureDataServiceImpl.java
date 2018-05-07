package com.microwise.proxima.service.impl;

import com.microwise.proxima.bean.InfraredPictureDataBean;
import com.microwise.proxima.dao.InfraredPictureDataDao;
import com.microwise.proxima.service.InfraredPictureDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * 红外图片数据信息服务层实现
 * </pre>
 *
 * @author li.jianfei
 * @date 2012-09-03
 */

@Service
@Scope("prototype")
@Transactional
public class InfraredPictureDataServiceImpl implements
		InfraredPictureDataService {
	
	@Autowired
	private InfraredPictureDataDao infraredPictureDataDao; 
	
	@Override
	public void save(InfraredPictureDataBean infraredPictureData) {
		infraredPictureDataDao.save(infraredPictureData);
	}

	@Override
	public InfraredPictureDataBean findById(String uuid) {
		return infraredPictureDataDao.findById(uuid);
	}

	@Override
	public InfraredPictureDataBean findByPicId(String picId) {
		return infraredPictureDataDao.findByPicId(picId);
	}


}
