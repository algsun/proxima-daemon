package com.microwise.proxima.dao;

import com.microwise.proxima.bean.MarkSegmentPositionBean;
import com.microwise.proxima.beans2.IdentityCode;
import com.microwise.proxima.dao.base.BaseDao;

import java.util.List;

/**
 * @author gaohui
 * @date 13-6-13 14:31
 */
public interface MarkSegmentPositionDao extends BaseDao<MarkSegmentPositionBean> {

    /**
     * 返回某个图片之前最后一个有值的标记段
     *
     * @param markSegmentId
     * @param pictureId
     * @return
     */
    MarkSegmentPositionBean findLatestBefore(String markSegmentId, String pictureId);

    /**
     * 获取图片二维码
     *
     * @return
     */
    public List<IdentityCode> findIdentityCodes(String pictureId);
}
