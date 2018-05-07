package com.microwise.proxima.bean;

/**
 * FTP 链接信息
 *
 * @author gaohui
 * @date 13-3-19 15:50
 * @author song.tao 2014-6-23
 */
public class FTPProfile {
    private String id;          //id号
    private String name;        //Ftp备注名称
    private String host;        //主机地址
    private int port;           //端口
    private String username;    //账户
    private String password;    //密码
    private Site site;          //所属站点

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
