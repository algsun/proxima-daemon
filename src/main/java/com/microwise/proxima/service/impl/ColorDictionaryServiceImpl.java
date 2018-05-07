package com.microwise.proxima.service.impl;

import com.microwise.proxima.bean.ColorDictionaryBean;
import com.microwise.proxima.dao.ColorDictionaryDao;
import com.microwise.proxima.service.ColorDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <pre>
 * 色轮字典信息服务层实现
 * </pre>
 * 
 * @author li.jianfei
 * @date 2012-09-03
 */

@Service
@Scope("prototype")
@Transactional
public class ColorDictionaryServiceImpl implements ColorDictionaryService {

	@Autowired
	private ColorDictionaryDao colorDictionaryDao;

	@Override
	public int save(ColorDictionaryBean colorDictionary) {
		return (Integer) colorDictionaryDao.save(colorDictionary);
	}

	@Override
	public ColorDictionaryBean findById(int id) {
		return colorDictionaryDao.findById(id);
	}

	@Override
	public List<ColorDictionaryBean> findAll() {
		return colorDictionaryDao.findAll();
	}

}
