package com.microwise.proxima.imagesync;

import com.microwise.proxima.bean.*;
import com.microwise.proxima.imagesync.ext.ImageSyncListener;
import com.microwise.proxima.imagesync.ext.ListenerWrapTask;
import com.microwise.proxima.imagesync.ext.SerializeMultiTask;
import com.microwise.proxima.service.PictureService;
import com.microwise.proxima.util.IdUtil;
import com.microwise.proxima.util.qiniu.QiniuUtil;
import it.sauronsoftware.ftp4j.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 图片扫描处理
 * <p>
 * <p>
 * <p>
 * <pre>
 * 步骤：
 * 1、获取摄像机信息。
 * 2、检测项目目录下有没有图片保存目录。
 * 3、扫描图片将图片信息保存到数据库。
 * 4、将图片移动到图片保存目录，同时将ftp服务器上的图片移动到ftp服务中其它目录中，以作备份。
 * </pre>
 *
 * @author zhang.licong
 * @author gaohui
 * @author song.tao 2014-6-23
 * @date 2012-7-6
 * @date 2013-03-22
 * @check @xubaoji #2293 2013-03-29
 * @check li.jianfei liu.zhu #8263 2014-4-15
 */
public class PictureScan {

    public static final Logger log = Logger.getLogger(PictureScan.class);

    // 常量设置
    private static final String IMAGE_FORMAT = ".jpg"; // 图片格式
    private static final String FTP_IMAGE_BACKUP_DIRECTORY = "backups";// FTP图片备份目录

    @Autowired
    private PictureService pictureService;// 图片service注入
    @Autowired
    private TaskExecutor taskExecutor;

    private List<ImageSyncListener> imageSyncListeners = new ArrayList<ImageSyncListener>();
    private boolean uploadToCloud;

    /**
     * 根据bean类型对红外、光学图片进行预处理
     */
    public void readyPictureProcess(FTPClient client, DVPlaceBean dvPlace) {
        try {
            if (dvPlace instanceof InfraredDVPlaceBean) {           // 执行红外图片扫描主程序
                infraredPictureScan(client, dvPlace);
            } else if (dvPlace instanceof OpticsDVPlaceBean) {      // 执行光学图片扫描主程序
                opticsPictureScan(client, dvPlace);
            }
        } catch (Exception e) {
            log.error("图片扫描异常", e);
        }
    }

    /**
     * 红外摄像机图片处理
     *
     * @param client      客户端
     * @param dvPlaceBean 规则名称
     * @return
     * @throws FTPException
     * @throws Exception
     * @author zhang.licong
     * @date 2012-7-9
     */
    public boolean infraredPictureScan(FTPClient client, DVPlaceBean dvPlaceBean) throws Exception {

        // 获取规则名称（点位编码）
        String ruleName = dvPlaceBean.getPlaceCode();

        // 罗列当前目录中的红外摄像机文件
        FTPFile[] ftpPictures = null;
        try {
            ftpPictures = client.list(ruleName + "*" + IMAGE_FORMAT);
            // 如果文件或者文件夹不存在直接返回
            if (ftpPictures == null || ftpPictures.length < 1) {
                return false;
            }
        } catch (FTPException e) {
            // 文件或目录不存在
            if (e.getCode() == FTPCodes.FILE_NOT_FOUND) {
                log.info("文件或目录不存在 " + dvPlaceBean.getPlaceCode(), e);
                return false;
            }
            throw e;
        }

        // 获取项目目录绝对路径
        String path = InitJobBean.getImageResourcesRootDirPath();
        // 创建项目下的图片目录如果不存在
        File dvPlacePictureDir = ensureDirectoryExists(ruleName, path);

        sortFTPFiles(ftpPictures);
        String pictureDirPath = dvPlacePictureDir.getAbsolutePath();
        String absolutePath = pictureDirPath.substring(pictureDirPath.lastIndexOf("proxima"));
        for (FTPFile f : ftpPictures) {

            // 图片名称
            String pictureName = f.getName();
            // 图片文件对象
            File file;
            try {
                // 将图片保存到项目upload目录中
                file = new File(dvPlacePictureDir, pictureName);
                client.download(pictureName, file);
                if (uploadToCloud) {
                    QiniuUtil.upload(file, absolutePath + File.separator + pictureName);
                }
            } catch (FTPException ex) {
                log.error("download image error, dir:" + dvPlacePictureDir + ", pictureName:" + pictureName);
                continue;
            }

            // 移动图片
            this.movePicture(client, ruleName, pictureName, client.currentDirectory());

            // 保存图片信息到数据库
            PictureBean picture = this.savePicture(pictureName, f.getModifiedDate(), ruleName, dvPlaceBean);

            fireListeners(dvPlaceBean, file, picture, DVType.INFRARED);
        }

        return true;
    }

    /**
     * 光学摄像机图片处理
     *
     * @param client      客户端
     * @param dvPlaceBean 规则名称
     * @return
     * @author zhang.licong
     * @date 2012-7-9
     */
    public boolean opticsPictureScan(FTPClient client, DVPlaceBean dvPlaceBean) throws Exception {
        String ruleName = dvPlaceBean.getPlaceCode();
        if (!ftpDirectoryExists(client, "/" + ruleName)) {
            return false;
        }
        log.debug("摄像机图片扫描开始, CODE: " + dvPlaceBean.getPlaceCode());
        // 进入目录
        client.changeDirectory("/" + ruleName);

        // 获取项目绝对路径
        String path = InitJobBean.getImageResourcesRootDirPath();

        // 创建项目下的图片目录
        File dvPlacePictureDir = ensureDirectoryExists(ruleName, path);

        // 罗列该目录下所有图片
        FTPFile[] ftpPictures = null;
        try {
            ftpPictures = client.list("*" + IMAGE_FORMAT);
            if (ftpPictures == null || ftpPictures.length < 1) {
                return false;
            }
        } catch (FTPException e) {
            if (e.getCode() == FTPCodes.FILE_NOT_FOUND) {
                // 文件或目录不存在
                log.debug("文件或目录不存在 " + dvPlaceBean.getPlaceCode(), e);
                return false;
            }
            throw e;
        }

        // 按最后修改时间排序( 这样有利于计算标记段，标记段有 delta 所以会前后依赖 )
        sortFTPFiles(ftpPictures);
        String pictureDirPath = dvPlacePictureDir.getAbsolutePath();
        String absolutePath = pictureDirPath.substring(pictureDirPath.lastIndexOf("proxima"));
        for (FTPFile ftpFile : ftpPictures) {
            // 图片名称
            String pictureName = ftpFile.getName();

            // 将图片保存到项目资源目录中
            File localFile = new File(dvPlacePictureDir, pictureName);
            try {
                client.download(pictureName, localFile);
                if (uploadToCloud) {
                    QiniuUtil.upload(localFile, absolutePath + File.separator + pictureName);
                }
            } catch (Exception e) {
                log.error("download image error, dir:" + dvPlacePictureDir + ", pictureName:" + pictureName);
                continue;
            }
            // 将FTP服务器上的当前文件给放到备份目录backups中
            this.movePicture(client, ruleName, pictureName, client.currentDirectory());

            // 保存图片信息到数据库
            PictureBean picture = this.savePicture(pictureName, ftpFile.getModifiedDate(), ruleName, dvPlaceBean);
            fireListeners(dvPlaceBean, localFile, picture, DVType.OPTICS);
        }

        return true;
    }

    /**
     * 保存图片信息
     *
     * @author zhang.licong
     * @date 2012-7-11
     */
    private PictureBean savePicture(String picturesName, Date date,
                                    String path, DVPlaceBean dVPlaceBean) throws Exception {
        PictureBean pb = new PictureBean();
        try {
            // 保存到数据库
            pb.setId(IdUtil.get64UUID());
            pb.setName(picturesName);
            pb.setPath(path);
            pb.setSaveTime(date);
            pb.setDv(dVPlaceBean);
            pb.setAnalyzable(true);

            this.pictureService.savePicture(pb);
            log.debug("保存图片信息到数据库....");
        } catch (Exception e) {
            log.error("保存图片信息到数据库失败", e);
        }

        return pb;
    }

    /**
     * 按最后修改时间排序
     *
     * @param ftpFiles
     */
    private static void sortFTPFiles(FTPFile[] ftpFiles) {
        Arrays.sort(ftpFiles, new Comparator<FTPFile>() {
            @Override
            public int compare(FTPFile o1, FTPFile o2) {
                if (o1.getModifiedDate().before(o2.getModifiedDate())) {
                    return -1;
                } else if (o1.getModifiedDate().after(o2.getModifiedDate())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * 触发图片监听器
     * <p>
     * 如果 onImageSync 方法执行需要比较长的时间，会影响图片入库的即时性，
     * 所以这里将 listener 的执行放在线程池中
     *
     * @param dvPlaceBean
     * @param localFile
     * @param picture
     */
    private void fireListeners(DVPlaceBean dvPlaceBean, File localFile, PictureBean picture, DVType dvType) {
        // 通知图片解析程序
        List<Runnable> tasks = new ArrayList<Runnable>();
        for (ImageSyncListener imageSyncListener : imageSyncListeners) {
            Runnable task = new ListenerWrapTask(imageSyncListener, localFile, dvType, dvPlaceBean, picture);
            tasks.add(task);
        }

        taskExecutor.execute(new SerializeMultiTask(tasks));
    }

    /**
     * 判断项目目录中图片文件夹是否存在
     *
     * @param ruleName 文件夹名称
     * @param path     项目路径
     * @author zhang.licong
     * @date 2012-7-11
     */
    private File ensureDirectoryExists(String ruleName, String path) {
        // 创建摄像机图片文件夹
        File directory = new File(path, ruleName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    /**
     * 将FTP服务器上的当前文件给放到备份目录backups中
     *
     * @author zhang.licong
     * @date 2012-7-11
     */
    private void movePicture(FTPClient client, String ruleName, String picturesName, String path) throws Exception {
        try {
            // 进入根目录，确保当前目录是根目录
            client.changeDirectory("/");
            if (!FTPUtils.exists(client, FTP_IMAGE_BACKUP_DIRECTORY)) {
                // 如果不存在就创建
                client.createDirectory(FTP_IMAGE_BACKUP_DIRECTORY);
            }
            // 进入backups目录
            client.changeDirectory("/" + FTP_IMAGE_BACKUP_DIRECTORY + "/");

            // 验证backups目录下的本摄像机目录是否存在
            if (!FTPUtils.exists(client, ruleName)) {
                // 如果不存在就创建
                client.createDirectory(ruleName);
            }

            // 进入根目录
            client.changeDirectory(path);


            // 移动图片位置
            client.rename(picturesName, "/" + FTP_IMAGE_BACKUP_DIRECTORY + "/" + ruleName + "/" + picturesName);
        } catch (IllegalStateException | IOException | FTPIllegalReplyException e) {
            log.error("图片备份", e);
        } catch (FTPException e) {
            log.error("图片备份", e);
            client.deleteFile(picturesName);
        }
    }

    /**
     * 判断文件夹是否存在
     *
     * @param ftpClient
     * @param path
     * @return
     * @throws IOException
     * @throws FTPIllegalReplyException
     * @throws FTPException
     */
    private static boolean ftpDirectoryExists(FTPClient ftpClient, String path) throws IOException, FTPIllegalReplyException, FTPException {
        try {
            ftpClient.changeDirectory(path);
        } catch (FTPException e) {
            if (e.getCode() == FTPCodes.FILE_NOT_FOUND) {
                return false;
            }
            throw e;
        }

        // 恢复到根目录
        ftpClient.changeDirectory("/");
        return true;
    }

    public void setImageSyncListeners(List<ImageSyncListener> imageSyncListeners) {
        this.imageSyncListeners = imageSyncListeners;
    }

    public void setUploadToCloud(boolean uploadToCloud) {
        this.uploadToCloud = uploadToCloud;
    }
}
