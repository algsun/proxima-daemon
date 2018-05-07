package com.microwise.proxima.infraredImageResolution;

import com.microwise.proxima.bean.ColorDictionaryBean;
import com.microwise.proxima.service.ColorDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 从数据库中加载 ColorWheel
 * <p>
 * 单例
 * 
 * @author gaohui
 * @date 2012-9-11
 * 
 * @check guo.tian li.jianfei 2012-09-19
 */
@Component
@Scope("singleton")
public class ColorWheelHolder {
	private ColorWheel colorWheel;

	@Autowired
	public ColorWheelHolder(ColorDictionaryService colorDictionaryService) {
		List<ColorDictionaryBean> list = colorDictionaryService.findAll();
		int[] rgbSAverage = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			rgbSAverage[i] = list.get(i).getColorRGB();
		}
		colorWheel = new ColorWheel(rgbSAverage);
	}

	public ColorWheel getColorWheel() {
		return colorWheel;
	};

}
