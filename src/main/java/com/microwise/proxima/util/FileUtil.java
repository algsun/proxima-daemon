package com.microwise.proxima.util;

import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Date: 12-8-22
 * Time: 下午11:03
 *
 * @author bastengao
 */
public class FileUtil {
    /**
     * 获取 classpath 下在资源文件路径
     *
     * @param resourcePath
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String filePathInClasspath(String resourcePath) throws UnsupportedEncodingException {
        URL url = Resources.getResource(resourcePath);
        return URLDecoder.decode(url.getFile(), "UTF-8");
    }

    public static InputStream fileInputStreamInClasspath(String resourcePath) throws IOException {
        return Resources.newInputStreamSupplier(Resources.getResource(resourcePath)).getInput();
    }
}
