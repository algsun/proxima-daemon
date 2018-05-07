package com.microwise.proxima.bean;


/**
 * 二维码标记段
 *
 * @author gaohui
 * @date 13-3-18 16:36
 */
public class QRMarkSegmentBean extends MarkSegmentBean {
    /**
     * A标记码的文本
     */
    private String textA;

    /**
     * B标记码的文本
     */
    private String textB;

    public String getTextA() {
        return textA;
    }

    public void setTextA(String textA) {
        this.textA = textA;
    }

    public String getTextB() {
        return textB;
    }

    public void setTextB(String textB) {
        this.textB = textB;
    }
}
