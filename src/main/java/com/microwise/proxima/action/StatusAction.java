package com.microwise.proxima.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.google.common.base.Strings;
import com.jcabi.manifests.Manifests;
import com.microwise.proxima.bean.FTPProfile;
import com.microwise.proxima.service.FTPProfileService;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 返回应用状态
 *
 * @author gaohui
 * @date 14-4-2 11:02
 */
@Component
@Scope("prototype")
public class StatusAction {

    @Autowired
    private FTPProfileService ftpProfileService;

    private String name = "proxima-daemon";
    private String version = null;
    private long uptime;
    private String uptime_str = null;
    private int svn_revision;
    private String msg;

    @Route("/status.json")
    public String view() {
        Date startTime = (Date) ServletActionContext.getServletContext().getAttribute("app.startTime");
        uptime = startTime.getTime();
        uptime_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
        String svnRevision = readManifest("App-Svn-Revision");
        if (!Strings.isNullOrEmpty(svnRevision)) {
            svn_revision = Integer.parseInt(svnRevision);
        }
        String appVersion = readManifest("App-Version");
        if (!Strings.isNullOrEmpty(appVersion)) {
            version = appVersion;
        }

        return Results.json().done();
    }


    @Route("/ftpConnect.json")
    public String ftpConnect() {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();

        List<FTPProfile> ftpProfiles = ftpProfileService.findAllFtp();
        if(ftpProfiles==null){
          return null;
        }
        for (FTPProfile ftpProfile:ftpProfiles) {
            Map<String, Object> map = ftpProfileService.connectFtp(ftpProfile);
            maps.add(map);
        }
        return Results.json().asRoot(maps).done();
    }

    private static String readManifest(String key) {
        try {
            return Manifests.read(key);
        } catch (IllegalArgumentException e) { // do nothing
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getUptime_str() {
        return uptime_str;
    }

    public long getUptime() {
        return uptime;
    }

    public int getSvn_revision() {
        return svn_revision;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
