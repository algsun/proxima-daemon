package com.microwise.proxima.util;

import com.google.common.io.Resources;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 获取项目目录工具栏
 * 
 * @author zhang.licong
 * @date 2012-7-10
 * 
 */
public class PathUtil {
	public static void main(String[] args) {
		PathUtil pathUtil = new PathUtil();
		System.out.println("path==" + pathUtil.getWebInfPath());
	}

	public String getWebInfPath() {
		URL url = getClass().getProtectionDomain().getCodeSource()
				.getLocation();
		String path = url.toString();
		int index = path.indexOf("WEB-INF");

		if (index == -1) {
			index = path.indexOf("classes");
		}

		if (index == -1) {
			index = path.indexOf("bin");
		}

		path = path.substring(0, index);

		if (path.startsWith("zip")) {// 当class文件在war中时，此时返回zip:D:/...这样的路径
			path = path.substring(4);
		} else if (path.startsWith("file")) {// 当class文件在class文件中时，此时返回file:/D:/...这样的路径
			path = path.substring(6);
		} else if (path.startsWith("jar")) {// 当class文件在jar文件里面时，此时返回jar:file:/D:/...这样的路径
			path = path.substring(10);
		}
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return path;
	}

	public static String getWebInfPath2() {
		URL url = Resources.getResource("hibernate.cfg.xml");
		
		try {
			File strutsXml = new File(URLDecoder.decode( url.getFile(),"UTF-8"));
			String webroot = new File(new File(strutsXml.getParent()).getParent()).getParent();
			return webroot;
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

}
