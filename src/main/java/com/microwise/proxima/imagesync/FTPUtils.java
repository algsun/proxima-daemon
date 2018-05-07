package com.microwise.proxima.imagesync;

import com.google.common.base.Strings;
import com.microwise.proxima.bean.FTPProfile;
import com.microwise.proxima.exception.FtpFailure;
import it.sauronsoftware.ftp4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * FTP工具类
 *
 * @author zhang.licong
 * @author gaohui
 * @author song.tao 2014-6-23
 * @author song.tao 2014-6-23
 * @date 2012-7-9
 * @date 2013-03-22
 * @check @xubaoji #2352 2013-03-29
 */
public class FTPUtils {
    public static final Logger log = LoggerFactory.getLogger(FTPUtils.class);

    protected FTPUtils() {
    }

    /**
     * FTP登陆(获取FTP客户端)
     *
     * @return FTPClient
     * @author zhang.licong
     * @date 2012-7-9
     */
    public static FTPClient ftpLogin(FTPProfile ftpProfile) throws FtpFailure {
        FTPClient client = new FTPClient();

        try {
            client.connect(ftpProfile.getHost(), ftpProfile.getPort());        // 连接
            client.login(ftpProfile.getUsername(), ftpProfile.getPassword());  // 登陆
        } catch (IOException e) {
            log.error("FTP网络异常" + ftpToString(ftpProfile), e);
            throw new FtpFailure(e);
        } catch (FTPIllegalReplyException e) {
            log.error("FTP非法回复" + ftpToString(ftpProfile), e);
            throw new FtpFailure(e);
        } catch (FTPException e) {
            if (e.getCode() == FTPCodes.NOT_LOGGED_IN) {
                log.error("FTP用户名或者密码错误" + ftpToString(ftpProfile), e);
            } else {
                log.error("FTP错误" + ftpToString(ftpProfile), e);
            }
            throw new FtpFailure(e);
        }
        return client;
    }

    /**
     * 注销客户端连接
     *
     * @param client FTP客户端对象
     * @author zhang.licong
     * @date 2012-7-9
     */
    public static void ftpLogout(FTPClient client) {
        if (client == null) return;
        if (client.isConnected()) {
            try {
                // 正常断开
                client.disconnect(true);
            } catch (Exception e) {
                try {
                    // 强制断开
                    client.disconnect(false);
                } catch (Exception e1) {
                    log.error("FTP断开连接操作失败", e);
                }
                log.error("FTP断开连接操作失败", e);
            }
        }
    }


    /**
     * 创建目录
     *
     * @param client FTP客户端对象
     * @param dir    目录
     * @throws Exception
     */
    public static void mkdirs(FTPClient client, String dir) throws Exception {
        try {
            if (dir == null) {
                return;
            }
            dir = dir.replace("//", "/");
            String[] dirs = dir.split("/");
            for (String dir1 : dirs) {
                dir = dir1;
                if (!Strings.isNullOrEmpty(dir)) {
                    if (!exists(client, dir)) {
                        client.createDirectory(dir);
                    }
                    client.changeDirectory(dir);
                }
            }
        } catch (Exception e) {
            log.error("FTP操作失败", e);
        }
    }

    /**
     * 获取FTP目录
     *
     * @param url 原FTP目录
     * @param dir 目录
     * @return
     * @throws Exception
     */
    public static URL getURL(URL url, String dir) throws Exception {
        String path = url.getPath();
        if (!path.endsWith("/") && !path.endsWith("//")) {
            path += "/";
        }
        dir = dir.replace("//", "/");
        if (dir.startsWith("/")) {
            dir = dir.substring(1);
        }
        path += dir;
        return new URL(url, path);
    }


    /**
     * 判断文件或目录是否存在
     *
     * @param client FTP客户端对象
     * @param dir    文件或目录
     * @return
     * @throws Exception
     */
    public static boolean exists(FTPClient client, String dir) throws Exception {
        return getFileType(client, dir) != -1;
    }

    /**
     * 判断当前为文件还是目录
     *
     * @param client FTP客户端对象
     * @param dir    文件或目录
     * @return -1、文件或目录不存在 0、文件 1、目录
     * @throws Exception
     * @author zhang.licong
     * @date 2012-7-9
     */
    public static int getFileType(FTPClient client, String dir) throws Exception {
        FTPFile[] files = null;
        try {
            files = client.list(dir);
        } catch (Exception e) {
            return -1;
        }
        if (files.length > 1) {
            return FTPFile.TYPE_DIRECTORY;
        } else if (files.length == 1) {
            try {
                FTPFile f = files[0];
                if (f.getType() == FTPFile.TYPE_DIRECTORY) {
                    return FTPFile.TYPE_DIRECTORY;
                }
                String path = dir + "/" + f.getName();
                int len = client.list(path).length;
                if (len == 1) {
                    return FTPFile.TYPE_DIRECTORY;
                } else {
                    return FTPFile.TYPE_FILE;
                }
            } catch (Exception e) {
                return FTPFile.TYPE_FILE;
            }
        } else {
            try {
                client.changeDirectory(dir);
                client.changeDirectoryUp();
                return FTPFile.TYPE_DIRECTORY;
            } catch (Exception e) {
                return -1;
            }
        }
    }

    private static String ftpToString(FTPProfile ftpProfile) {
        return String.format("[%s:%s 用户名:%s]", ftpProfile.getHost(), ftpProfile.getPort(), ftpProfile.getUsername());
    }

}

