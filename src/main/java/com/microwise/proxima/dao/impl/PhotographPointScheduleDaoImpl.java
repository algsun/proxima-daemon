package com.microwise.proxima.dao.impl;

import com.microwise.proxima.bean.PhotographPointScheduleBean;
import com.microwise.proxima.dao.PhotographPointScheduleDao;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * PhotographPointScheduleDao implements
 * 
 * @author gaohui
 * @date 2012-8-1
 */
@Repository
@Scope("prototype")
public class PhotographPointScheduleDaoImpl extends
        BaseDaoImpl<PhotographPointScheduleBean> implements
        PhotographPointScheduleDao {

	public PhotographPointScheduleDaoImpl() {
		super(PhotographPointScheduleBean.class);
	}

}
