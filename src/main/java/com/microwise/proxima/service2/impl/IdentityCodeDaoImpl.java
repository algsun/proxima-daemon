package com.microwise.proxima.service2.impl;

import com.microwise.proxima.beans2.IdentityCode;
import com.microwise.proxima.dao.base.BaseDaoImpl;
import com.microwise.proxima.util.Beans;

/**
 * @author gaohui
 * @date 13-6-13 13:34
 */
@Beans.Dao
public class IdentityCodeDaoImpl extends BaseDaoImpl<IdentityCode> implements IdentityCodeDao {
    public IdentityCodeDaoImpl() {
        super(IdentityCode.class);
    }
}
