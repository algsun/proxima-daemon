package com.microwise.proxima.action;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.OpticsDVPlaceBean;
import com.microwise.proxima.imagesync.PictureScanTrigger;
import com.microwise.proxima.photograph.PhotographScheduler;
import com.microwise.proxima.service.DVPlaceService;
import com.opensymphony.xwork2.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author gaohui
 * @date 13-3-26 15:15
 * @check @xubaoji #2398  2013-03-29
 */
@Component("dvPlaceAction")
@Scope("prototype")
public class DVPlaceAction {
    public static final Logger log = LoggerFactory.getLogger(DVPlaceAction.class);

    /**
     * 摄像机 service
     */
    @Autowired
    private DVPlaceService dvPlaceService;
    /**
     * 图片扫描任务
     */
    @Autowired
    private PictureScanTrigger pictureScanTrigger;
    /**
     * 拍照计划
     */
    @Autowired(required = false)
    private PhotographScheduler photographScheduler;

    //input
    /**
     * 摄像机ID
     */
    private String dvPlaceId;
    /**
     * 是否启用
     */
    private boolean enable;

    //output
    /**
     * 是否成功
     */
    private boolean success;

    // 启用/停用
    public String enable() {
        try {
            DVPlaceBean dvPlace = dvPlaceService.findWithFtpById(dvPlaceId);
            if (enable) {
                pictureScanTrigger.addTrigger(dvPlace);
                if (couldPhotograph(dvPlace)) {
                    // 添加拍照计划
                    photographScheduler.addTriggerByDvId(dvPlace.getId());
                }
            } else {
                pictureScanTrigger.cancelTrigger(dvPlace);
                if (couldPhotograph(dvPlace)) {
                    OpticsDVPlaceBean opticsDVPlace = (OpticsDVPlaceBean) dvPlace;
                    // 取消拍照计划
                    photographScheduler.cancelAllTrigger(opticsDVPlace);
                }
            }
            success = true;
        } catch (Exception e) {
            success = false;
            log.error("启用/停用摄像机", e);
        }
        return Action.SUCCESS;
    }

    // 添加图片同步计划
    public String added() {
        try {
            DVPlaceBean dvPlace = dvPlaceService.findWithFtpById(dvPlaceId);
            pictureScanTrigger.addTrigger(dvPlace);
            if (couldPhotograph(dvPlace)) {
                // 添加拍照计划
                photographScheduler.addTriggerByDvId(dvPlace.getId());
            }
            success = true;
        } catch (Exception e) {
            success = false;
            log.error("添加摄像机", e);
        }
        return Action.SUCCESS;
    }

    /**
     * 摄像机 FTP 更改
     *
     * @return
     */
    public String ftpChanged() {
        try {
            DVPlaceBean dvPlace = dvPlaceService.findWithFtpById(dvPlaceId);
            pictureScanTrigger.cancelTrigger(dvPlace);
            pictureScanTrigger.addTrigger(dvPlace);
            success = true;
        } catch (Exception e) {
            success = false;
            log.error("重新加载图片同步任务", e);
        }
        return Action.SUCCESS;
    }

    /**
     * 是否可以有拍照计划
     *
     * @param dvPlaceBean
     * @return
     */
    private boolean couldPhotograph(DVPlaceBean dvPlaceBean) {
        if (photographScheduler == null) {
            return false;
        }

        // 如果是光学摄像机
        return dvPlaceBean instanceof OpticsDVPlaceBean;
    }

    public String getDvPlaceId() {
        return dvPlaceId;
    }

    public void setDvPlaceId(String dvPlaceId) {
        this.dvPlaceId = dvPlaceId;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
