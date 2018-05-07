package com.microwise.proxima.bean;

/**
 * 表示 二维码标记段 在一个图片上的一次值
 * <p/>
 * Date: 12-9-28 Time: 下午3:09
 *
 * @author bastengao
 */
public class QRMarkSegmentPositionBean extends MarkSegmentPositionBean {

    /**
     * 长度(单位像素)
     *
     * @deprecated
     */
    private float length;

    // textA
    private QRCodePosition qrCodeA;

    // textB
    private QRCodePosition qrCodeB;

    /**
     *
     * @return
     * @deprecated
     */
    public float getLength() {
        return length;
    }

    /**
     *
     * @param length
     * @deprecated
     */
    public void setLength(float length) {
        this.length = length;
    }

    public QRCodePosition getQrCodeA() {
        return qrCodeA;
    }

    public void setQrCodeA(QRCodePosition qrCodeA) {
        this.qrCodeA = qrCodeA;
    }

    public QRCodePosition getQrCodeB() {
        return qrCodeB;
    }

    public void setQrCodeB(QRCodePosition qrCodeB) {
        this.qrCodeB = qrCodeB;
    }
}
