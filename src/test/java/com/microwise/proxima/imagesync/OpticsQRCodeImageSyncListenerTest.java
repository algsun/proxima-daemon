package com.microwise.proxima.imagesync;

import com.microwise.proxima.bean.MarkSegmentPositionBean;
import com.microwise.proxima.imagesync.ext.OpticsQRCodeImageSyncListener;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author gaohui
 * @date 13-6-24 14:46
 */
public class OpticsQRCodeImageSyncListenerTest {

    @Test
    public void testLength(){
        MarkSegmentPositionBean msp = new MarkSegmentPositionBean();
        msp.setPositionX(0);
        msp.setPositionY(0);
        msp.setPositionX2(3);
        msp.setPositionY2(4);

        float length = OpticsQRCodeImageSyncListener.length(msp);
        Assert.assertEquals(5, length, 0);
    }
}
