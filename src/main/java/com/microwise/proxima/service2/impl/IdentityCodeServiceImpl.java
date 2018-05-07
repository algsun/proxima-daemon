package com.microwise.proxima.service2.impl;

import com.microwise.proxima.beans2.IdentityCode;
import com.microwise.proxima.service2.IdentityCodeService;
import com.microwise.proxima.util.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author gaohui
 * @date 13-6-13 13:35
 */
@Beans.Service
@Transactional
public class IdentityCodeServiceImpl implements IdentityCodeService {
    @Autowired
    private IdentityCodeDao identityCodeDao;


    @Override
    public Serializable save(IdentityCode identityCode) {
        return identityCodeDao.save(identityCode);
    }

}
