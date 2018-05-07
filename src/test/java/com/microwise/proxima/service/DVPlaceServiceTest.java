package com.microwise.proxima.service;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author gaohui
 * @date 13-3-22 14:47
 */
public class DVPlaceServiceTest {
    @Ignore
    @Test
    public void testCount() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        DVPlaceService dvPlaceService = appContext.getBean(DVPlaceService.class);
        System.out.println(dvPlaceService.findEnableCount());
    }

    @Ignore
    @Test
    public void testFindAllEnable(){
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        DVPlaceService dvPlaceService = appContext.getBean(DVPlaceService.class);
        System.out.println(dvPlaceService.findAllEnable(0, 100));
    }
}
