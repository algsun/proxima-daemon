package com.microwise.proxima.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.google.common.base.Strings;
import com.jcabi.manifests.Manifests;
import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.FTPProfile;
import com.microwise.proxima.bean.OpticsDVPlaceBean;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.beans2.IdentityCode;
import com.microwise.proxima.photograph.RemoteIOServerHolder;
import com.microwise.proxima.service.FTPProfileService;
import com.microwise.proxima.service.MarkSegmentPositionService;
import com.microwise.proxima.service.OpticsDVPlaceService;
import com.microwise.proxima.service.PictureService;
import com.microwise.proxima.util.Configs;
import com.microwise.proxima.util.remoteio.RemoteIO;
import com.microwise.proxima.util.remoteio.server.RemoteIOServer;
import com.microwise.proxima.util.remoteio.server.ServerModeRemoteIO;
import com.opensymphony.xwork2.Action;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author liuzhu
 * @date 14-2-10
 */
@Component
@Scope("prototype")
public class IndexAction implements ApplicationContextAware {

    private static ApplicationContext applicationContext; // Spring应用上下文环境

    private static final Logger log = LoggerFactory.getLogger(IndexAction.class);

    @Autowired
    private OpticsDVPlaceService dvPlaceService;
    @Autowired
    private FTPProfileService ftpProfileService;
    @Autowired
    private PictureService pictureService;

    @Autowired
    private MarkSegmentPositionService markSegmentPositionService;

    // output
    /**
     * 继电器路数开关Map(key:端口，value：开关)
     */
    private Map<String, List<String>> onOrOffMap = new HashMap<String, List<String>>();
    // svn 版本号
    private int svnRevision;

    private List<? extends DVPlaceBean> dvPlaces;

    private List<Map<String, Object>> ftpList = new ArrayList<Map<String, Object>>();

    //最后一次拍照时间
    private Map<String, Object> lastPhotoTime = new HashMap<String, Object>();

    private int serverPort = Integer.parseInt(Configs.get("remoteIO.serverPort"));

    private int sessionCount;
    private List<IdentityCode> identityCodeList;

    //用端口号做key取通信时间
    private Map<Integer, Map<String, Object>> currentState = new HashMap<Integer, Map<String, Object>>();

    private IndexAction() {
    }

    public String execute() {
        try {
            initSvnRevision();

            dvPlaces = dvPlaceService.findAllEnable();

            for (DVPlaceBean dvPlaceBean : dvPlaces) {
                PictureBean pictureBean = pictureService.findNewPictures(dvPlaceBean.getId());
                if (pictureBean != null) {
                    pictureBean.setIdentityCodeList(markSegmentPositionService.findIdentityCodes(pictureBean.getId()));
                    dvPlaceBean.setPictureBean(pictureBean);
                }
            }

            List<FTPProfile> ftpProfiles = ftpProfileService.findAllFtp();
            if (ftpProfiles != null) {
                for (FTPProfile ftpProfile : ftpProfiles) {
                    Map<String, Object> map = ftpProfileService.connectFtp(ftpProfile);
                    ftpList.add(map);
                }

            }


            lastPhotoTime = pictureService.findLastPhotoTime(dvPlaces);
            if (RemoteIOServer.remoteIOHandler != null) {
                for (ConcurrentMap.Entry<InetSocketAddress, ServerModeRemoteIO> entry : RemoteIOServer.remoteIOHandler.getRemoteIOs().entrySet()) {
                    RemoteIOServer remoteIOServer = RemoteIOServerHolder.getInstance().getRemoteIOServer();
                    String ioHost = entry.getKey().getAddress().getHostAddress();
                    int ioPort = entry.getKey().getPort();
                    List<String> onOrOffStr = remoteIOServer.getRemoteIO(ioPort).findState();
                    onOrOffMap.put(String.valueOf(entry.getKey().getPort()), onOrOffStr);
                    RemoteIO remoteIO = remoteIOServer.getRemoteIO(ioPort);
                    Map<String, Object> map = new HashMap<String, Object>();
                    //摄像机列表
                    List<OpticsDVPlaceBean> opticsDVPlaces = dvPlaceService.findByIO(ioHost, ioPort);
                    if (opticsDVPlaces != null) {
                        for (OpticsDVPlaceBean opticsDVPlace : opticsDVPlaces) {
                            remoteIO.setIoVersion(opticsDVPlace.getIoVersion());
                            map.put("ioVersion", opticsDVPlace.getIoVersion());
                        }
                    }else{
                        //设置默认的版本号为2，如果有摄像机，取摄像机的ioVersion。
                        remoteIO.setIoVersion(2);
                        map.put("ioVersion", 2);
                    }
                    Object obj = null;
                    obj = remoteIO.findCurrentState();
                    map.put("channelState", obj);
                    String date;
                    if (obj != null) {
                        date = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
                        ((XmlWebApplicationContext) applicationContext).getServletContext().setAttribute("connectTime", date);
                    } else {
                        date = (String) ((XmlWebApplicationContext) applicationContext).getServletContext().getAttribute("connectTime");
                    }
                    map.put("connectTime", date);
                    currentState.put(ioPort, map);
                }
            }
            NioSocketAcceptor acceptor = (NioSocketAcceptor) ((XmlWebApplicationContext) applicationContext).getServletContext().getAttribute("acceptor");
            if (acceptor != null) {
                sessionCount = acceptor.getManagedSessionCount();
            }
        } catch (Exception e) {
            log.error("获取io路数开关异常:", e);
        }
        return Action.SUCCESS;
    }

    @Route("/findCurrentState")
    public String findCurrentState() {
        List<Map<String, Object>> currentState = new ArrayList<Map<String, Object>>();
        try {
            if (RemoteIOServer.remoteIOHandler != null) {
                for (ConcurrentMap.Entry<InetSocketAddress, ServerModeRemoteIO> entry : RemoteIOServer.remoteIOHandler.getRemoteIOs().entrySet()) {
                    RemoteIOServer remoteIOServer = null;
                    try {
                        remoteIOServer = RemoteIOServerHolder.getInstance().getRemoteIOServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("获取IO模块状态异常", e);
                    }
                    String ioHost = entry.getKey().getAddress().getHostAddress();
                    int ioPort = entry.getKey().getPort();

                    Object obj = null;
                    RemoteIO remoteIO = remoteIOServer.getRemoteIO(ioPort);
                    List<OpticsDVPlaceBean> opticsDVPlaces = dvPlaceService.findByIO(ioHost, ioPort);
                    if (opticsDVPlaces != null) {
                        for (OpticsDVPlaceBean opticsDVPlace : opticsDVPlaces) {
                            remoteIO.setIoVersion(opticsDVPlace.getIoVersion());
                        }
                    }else{
                        //版本默认为2
                        remoteIO.setIoVersion(2);
                    }
                    obj = remoteIO.findCurrentState();
                    boolean channelStateFlag = false;
                    if (obj != null) {
                        channelStateFlag = true;
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("port", ioPort);
                    map.put("channelState", channelStateFlag);
                    map.put("connectTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
                    currentState.add(map);
                }
            }
        } catch (Exception e) {
            log.error("获取io模块状态异常:", e);
        }
        return Results.json().asRoot(currentState).done();
    }

    // 加载版本号
    private void initSvnRevision() {
        try {
            String svnRevisionStr = Manifests.read("App-Svn-Revision");
            if (!Strings.isNullOrEmpty(svnRevisionStr)) {
                svnRevision = Integer.parseInt(svnRevisionStr);
            }
        } catch (IllegalArgumentException e) {
            // do nothing
        }
    }


    public Map<String, List<String>> getOnOrOffMap() {
        return onOrOffMap;
    }

    public void setOnOrOffMap(Map<String, List<String>> onOrOffMap) {
        this.onOrOffMap = onOrOffMap;
    }

    public int getSvnRevision() {
        return svnRevision;
    }

    public void setSvnRevision(int svnRevision) {
        this.svnRevision = svnRevision;
    }

    public List<? extends DVPlaceBean> getDvPlaces() {
        return dvPlaces;
    }

    public void setDvPlaces(List<? extends DVPlaceBean> dvPlaces) {
        this.dvPlaces = dvPlaces;
    }

    public List<Map<String, Object>> getFtpList() {
        return ftpList;
    }

    public void setFtpList(List<Map<String, Object>> ftpList) {
        this.ftpList = ftpList;
    }

    public Map<String, Object> getLastPhotoTime() {
        return lastPhotoTime;
    }

    public void setLastPhotoTime(Map<String, Object> lastPhotoTime) {
        this.lastPhotoTime = lastPhotoTime;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public Map<Integer, Map<String, Object>> getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Map<Integer, Map<String, Object>> currentState) {
        this.currentState = currentState;
    }

    public List<IdentityCode> getIdentityCodeList() {
        return identityCodeList;
    }

    public void setIdentityCodeList(List<IdentityCode> identityCodeList) {
        this.identityCodeList = identityCodeList;
    }
}
