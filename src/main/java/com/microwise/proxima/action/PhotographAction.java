package com.microwise.proxima.action;

import com.google.common.base.Strings;
import com.microwise.proxima.photograph.PhotographJobInit;
import com.microwise.proxima.photograph.PhotographScheduler;
import com.opensymphony.xwork2.Action;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 与拍照计划有关的action ， 用来使用http 方式 与其他系统进行交互
 * 
 * @author xubaoji
 * @date 2013-3-26
 * @check @gaohui #2328 2013-03-29
 */
@Component
@Scope("prototype")
public class PhotographAction {

	public static final Logger log = LoggerFactory
			.getLogger(PhotographJobInit.class);

	/** 用来 添加或 删除 拍照计划的类 */
	@Autowired
	private PhotographScheduler photographScheduler;

	// input
	/** 摄像机编号 */
	private String dvPlaceId;

	// output
	/** 重置拍照计划结果返回值：true 成功，false 失败 */
	private boolean success;

	/** 服务出错时的信息提示 */
	private String message;
	
	/** 重置拍照计划*/
	public String refreshPhotographPlan() {
		log.info("重置摄像机拍照计划：" + dvPlaceId);
        if(Strings.isNullOrEmpty(dvPlaceId)) {
			success = false;
			message = "dvPlaceId参数错误：" + dvPlaceId;
			return Action.SUCCESS;
		}
		try {
			photographScheduler.cancelTriggerByDvId(dvPlaceId);
			photographScheduler.addTriggerByDvId(dvPlaceId);
			success = true;
		} catch (SchedulerException e) {
			log.error("摄像机拍照计划重置失败：" + dvPlaceId, e);
			message = "后台服务器出错";
			success = false;
		}
		return Action.SUCCESS;
	}

	public String getDvPlaceId() {
		return dvPlaceId;
	}

	public void setDvPlaceId(String dvPlaceId) {
		this.dvPlaceId = dvPlaceId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
