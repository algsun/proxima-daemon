package com.microwise.proxima.dao;

import com.microwise.proxima.bean.MarkSegmentBean;
import com.microwise.proxima.dao.base.BaseDao;

/**
 * @author gaohui
 * @date 13-6-13 14:17
 */
public interface MarkSegmentDao extends BaseDao<MarkSegmentBean> {
    MarkSegmentBean findByName(String dvPlaceId, String name);
}
