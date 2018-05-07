package com.microwise.proxima.action;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.imagesync.PictureScanTrigger;
import com.microwise.proxima.service.DVPlaceService;
import com.opensymphony.xwork2.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gaohui
 * @date 13-3-26 15:14
 * @check  @xubaoji #2312  2013-03-29
 */
@Component("ftpAction")
@Scope("prototype")
public class FTPAction {
    public static final Logger log = LoggerFactory.getLogger(FTPAction.class);

    @Autowired
    private DVPlaceService dvPlaceService;
    @Autowired
    PictureScanTrigger pictureScanTrigger;

    //input
    /**
     * ftpProfile ID
     */
    private String ftpProfileId;

    //output
    /**
     * 成功与否
     */
    private boolean success;

    public String update() {
        try {
            // 更新所有关联到的摄像机
            List<DVPlaceBean> dvPlaces = dvPlaceService.findAllEnableByFTP(ftpProfileId);
            for (DVPlaceBean dvPlace : dvPlaces) {
                pictureScanTrigger.cancelTrigger(dvPlace);
                pictureScanTrigger.addTrigger(dvPlace);
            }
            success = true;
        } catch (Exception e) {
            success = false;
            log.error("更新 FTPProfile", e);
        }
        return Action.SUCCESS;
    }

    public String getFtpProfileId() {
        return ftpProfileId;
    }

    public void setFtpProfileId(String ftpProfileId) {
        this.ftpProfileId = ftpProfileId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
