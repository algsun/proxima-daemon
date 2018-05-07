package com.microwise.proxima.service;

import com.microwise.proxima.bean.MarkSegmentBean;

import java.io.Serializable;

/**
 * service
 *
 * @author gaohui
 * @date 13-6-13 14:17
 */
public interface MarkSegmentService {
    Serializable save(MarkSegmentBean markSegmentBean);

    MarkSegmentBean findByName(String dvPlaceId, String name);
}
