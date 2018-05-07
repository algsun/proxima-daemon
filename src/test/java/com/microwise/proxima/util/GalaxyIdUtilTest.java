package com.microwise.proxima.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author gaohui
 * @date 13-3-22 16:36
 */
public class GalaxyIdUtilTest {
    @Test
    public void testGetUUID64() {
        String based64UUID = IdUtil.get64UUID();
        Assert.assertNotNull(based64UUID);
        Assert.assertEquals(22, based64UUID.length());
        System.out.println(based64UUID);
    }
}
