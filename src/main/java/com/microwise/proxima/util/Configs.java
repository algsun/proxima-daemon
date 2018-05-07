package com.microwise.proxima.util;

import com.google.common.io.Closeables;
import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 负责获取 config.properites 配置
 * 
 * @author gaohui
 * @date 2012-6-12
 */
public class Configs {

	private static Configs instance = new Configs();
	private Properties properties;

	private Configs() {
		try {
			//加载 "config.properties" 到 properties.
			InputStream input = Resources.newInputStreamSupplier(
					Resources.getResource("config.properties")).getInput();
			properties = new Properties();
			properties.load(input);
			Closeables.closeQuietly(input);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 获取配置中 key 对应的 value
	 * @param key
	 * @return
	 */
	private String getValue(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * 获取配置中 key 对应的 value
	 * @param key
	 * @return
	 */
	public static String get(String  key) {
		return instance.getValue(key);
	}
}
