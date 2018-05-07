package com.microwise.proxima.util.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by lijianfei on 15/12/25.
 */
public class QiniuUtil {
    public static final Logger log = LoggerFactory.getLogger(QiniuUtil.class);

    // 七牛 KEY
    private static final String ACCESS_KEY = "oUz3q-tWCeJIJ-H87qUGhh4wW-Io5LaBKRZ59G46";
    private static final String SECRET_KEY = "lYGasQURtgpUIbnCPKp1QvK14NimdURPK1VR1Hwj";
    private static final String DEFAULT_BUCKET = "proxima";


    private static String getUploadToken(String bucket, String key) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        return auth.uploadToken(bucket, key);
//        return auth.uploadToken(bucket, key, 3600, new StringMap()
//                .putNotEmpty("returnBody", "{\"key\": $(key), \"hash\": $(etag), \"width\": $(imageInfo.width), \"height\": $(imageInfo.height)}"));
    }

    public static void upload(File file, String key) {

        try {
            // 替換 \ 為 /
            key = key.replace("\\", "/");
            // 下载完成后将照片上传到七牛云存储
            Recorder recorder = new FileRecorder(file.getParent());
            UploadManager uploadManager = new UploadManager();
            Response res = uploadManager.put(file, key, getUploadToken(DEFAULT_BUCKET, key));
            if (res.isOK()) {
                DefaultPutRet ret = res.jsonToObject(DefaultPutRet.class);
                log.info(res.bodyString());
            } else {

            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时简单状态信息
            log.error(r.toString());
            try {
                // 响应的文本信息
                log.error(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

    private class MyRet {
        public long fsize;
        public String key;
        public String hash;
        public int width;
        public int height;
    }
}
