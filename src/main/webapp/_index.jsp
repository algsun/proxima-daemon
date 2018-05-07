<%@ page import="com.google.common.base.Strings" %>
<%@ page import="com.microwise.proxima.bean.DVPlaceBean" %>
<%@ page import="com.microwise.proxima.photograph.PhotographScheduler" %>
<%@ page import="com.microwise.proxima.util.remoteio.server.RemoteIOServer" %>
<%@ page import="com.microwise.proxima.util.remoteio.server.ServerModeRemoteIO" %>
<%@ page import="org.quartz.JobExecutionContext" %>
<%@ page import="org.quartz.Scheduler" %>
<%@ page import="org.quartz.Trigger" %>
<%@ page import="org.quartz.TriggerKey" %>
<%@ page import="org.quartz.impl.matchers.GroupMatcher" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.net.InetSocketAddress" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.concurrent.ConcurrentMap" %>
<%@ page import="com.microwise.proxima.beans2.IdentityCode" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.microwise.proxima.bean.PictureBean" %>
<%--
@author gaohui
@date 2013-03-21
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>守望者 - 本体监测后台</title>
    <link rel="shortcut icon" type="image/x-icon" href="favicon.ico"/>
    <link rel="stylesheet" href="assets/bootstrap/3.0.3/css/bootstrap.min.css">
</head>
<body style="padding: 10px 15px;">

<%
    ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(application);
    Scheduler scheduler = appContext.getBean(Scheduler.class);
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

<div class="container-fluid">
<h1 style="text-align: center;">守望者守望中......</h1>

<div class="row-fluid">
    <div class="col-md-5">
        <p><a href="status.json">状态</a> 版本号
            ：${requestScope.svnRevision}，启动时间：<%= dateFormat.format(application.getAttribute("app.startTime"))%>
        </p>

        <h3>摄像机</h3>
        <ul>
            <% for (DVPlaceBean dvPlace : (List<DVPlaceBean>) request.getAttribute("dvPlaces")) {
                String key = PhotographScheduler.triggerGroupName(dvPlace.getPlaceCode());
                Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(key));
            %>
            <li>
                <strong><%= dvPlace.getPlaceCode() %>
                </strong>
                <ul>
                    <li>下次拍照时间</li>
                    <%
                        Map<String, Object> lastPhotoTime = (Map<String, Object>) request.getAttribute("lastPhotoTime");
                        for (TriggerKey triggerKey : triggerKeys) {
                            Trigger trigger = scheduler.getTrigger(triggerKey);
                    %>
                    <li><%= dateFormat.format(trigger.getNextFireTime()) %>
                    </li>
                    <% } %>
                    <li>最近一次拍照时间</li>
                    <% if (lastPhotoTime.get(dvPlace.getId()) != null) { %>
                    <li>
                        <%=dateFormat.format(lastPhotoTime.get(dvPlace.getId()))%>
                    </li>
                    <% } %>
                </ul>
            </li>
            <% } %>
        </ul>

        <h3>ftp</h3>
        <ul id="ftpList">
            <% for (Map<String, Object> map : (List<Map<String, Object>>) request.getAttribute("ftpList")) {
            %>
            <li>
                <strong><%=map.get("ftp")%>
                </strong>
                <ul>
                    <li>ftp状态：
                        <% String statue = null;
                            if (map.get("isSuccess") != null) {
                                statue = "断开";
                            } else {
                                statue = "正常";
                            }
                        %>
                        <%=statue%>
                    </li>

                    <li>ftp最近一次链接时间 <%=map.get("connectDate")%>
                    </li>
                </ul>
            </li>
            <% } %>
        </ul>
    </div>
    <p>TCP监听端口: <strong style="margin: 10px"><%= request.getAttribute("serverPort")%>
    </strong>
        当前会话数为：
        <span id="isConnect"> <%=request.getAttribute("sessionCount")%></span>
    </p>

    <div class="col-md-7">

        <%
            if(RemoteIOServer.remoteIOHandler!=null){
        %>
        <h3>已连接的IO模块：<%=RemoteIOServer.remoteIOHandler.getRemoteIOs().size()%>个 </h3>
        <table class="table table-bordered">
            <tr>
                <th>端口</th>
                <th>IP</th>
                <th>路数状态</th>
                <th>通道状态</th>
                <th>连接时间</th>
                <th>操作</th>
            </tr>
            <%
                for (ConcurrentMap.Entry<InetSocketAddress, ServerModeRemoteIO> entry : RemoteIOServer.remoteIOHandler.getRemoteIOs().entrySet()) {
                    String ip = entry.getKey().getAddress().getHostAddress();
                    String port = String.valueOf(entry.getKey().getPort());
            %>
            <tr>
                <td id="port"><%=port%>
                </td>
                <td><%=ip%>
                </td>
                <td>
                    <% List<String> onOrOffList = ((Map<String, List<String>>) request.getAttribute("onOrOffMap")).get(port); %>
                    <% if(onOrOffList != null){ for (String s : onOrOffList) { %>
                    <%= s %>
                    <% }}else{%>
                    <%= "无"%>
                    <% }
                        Map<String, Object> currentState = ((Map<Integer, Map<String, Object>>) request.getAttribute("currentState")).get(Integer.parseInt(port));
                        String channelState = "";
                        if (currentState.get("channelState") != null) {
                            channelState = "正常";
                        } else {
                            channelState = "异常";
                        }
                    %>
                </td>
                <td>
                    <span id="channelState<%=port%>"> <%=channelState%></span>
                </td>
                <td>
                    <%
                        String connectTime = (String) currentState.get("connectTime");
                        String date;
                        if (Strings.isNullOrEmpty(connectTime)) {
                            date = "无";
                        } else {
                            date = connectTime;
                        }
                    %>
                    <span id="connectTime<%=port%>"> <%= date%></span>
                </td>
                <td>
                    <select id="<%=port%>" name="route" class="selectRoute">
                        <%
                            int route = 1;
                            if (session.getAttribute(port) != null) {
                                route = Integer.parseInt(session.getAttribute(port).toString());
                            }
                            for (int i = 1; i <= 4; i++) {
                        %>
                        <option value="<%=i%>" <% if(route==i){%>selected="selected" <%}%>  ><%=i%>路
                        </option>
                        <% } %>
                    </select>

                    <form style="display: inline-block;" name="turnOnFrom" action="turnOn.action">
                        <input type="hidden" name="port" value="<%=port%>">
                        <input type="hidden" name="route" class="route<%=port%>" value="<%=route%>">
                        <input type="hidden" name="ioVersion" value="<%=currentState.get("ioVersion")%>">
                        <button type="submit" class="btn btn-primary btn-xs">打开
                        </button>
                    </form>
                    <form style="display: inline-block;" name="turnOffFrom" action="turnOff.action">
                        <input type="hidden" name="port" value="<%=port%>">
                        <input type="hidden" name="route" class="route<%=port%>" value="<%=route%>">
                        <button type="submit" class="btn btn-danger btn-xs">关闭
                        </button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
        <%
        }
        %>
    </div>
</div>
<div class="row">


    <div class="col-md-6">
        <h3>triggers(触发器)</h3>
        <ul>
            <%-- 遍历 trigger group name --%>
            <% for (String triggerGroupName : scheduler.getTriggerGroupNames()) { %>
            <li>
                <%= triggerGroupName %>
                <ul>
                    <%-- 遍历一个 group 下的 所有 trigger  --%>
                    <% Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroupName)); %>
                    <% for (TriggerKey triggerKey : triggerKeys) { %>
                    <% Trigger trigger = scheduler.getTrigger(triggerKey); %>
                    <li>
                        <%= triggerKey.getName() %> [<%= trigger.getJobKey().getGroup()%>
                        /<%= trigger.getJobKey().getName()%>]
                    </li>
                    <% } %>
                </ul>
            </li>
            <%} %>
        </ul>
    </div>

    <div class="col-md-6">
        <h3>正在执行的任务</h3>
        <ul>
            <%
                for (JobExecutionContext exeCxt : scheduler.getCurrentlyExecutingJobs()) {
                    Trigger trigger = exeCxt.getTrigger();
                    String name = exeCxt.getJobDetail().getKey().getName();
                    String jobName = "";
                    if ("pictureScanJob".equals(name)) {
                        jobName = "图片扫描";
                    } else {
                        jobName = "拍照计划";
                    }
            %>
            任务：<%=jobName%>，开始时间：<%=dateFormat.format(trigger.getStartTime())%>，
            下次执行时间：<%=dateFormat.format(trigger.getNextFireTime())%>
            <% } %>
        </ul>
    </div>
    <div class="col-md-6">

        <h3>已扫描过的图片</h3>
        <table class="table table-bordered">
            <tr>
                <th>摄像机点位</th>
                <th>二维码名称</th>
                <th>点位编码</th>
                <th>时间</th>
            </tr>
            <%
                List<DVPlaceBean> dvPlaceBeans = (List<DVPlaceBean>) request.getAttribute("dvPlaces");
                for (DVPlaceBean dvPlaceBean : dvPlaceBeans) {
                    String placeName = dvPlaceBean.getPlaceName();

                    String textA = "";
                    String textB = "";
                    String text = "";
                    String placeCode = "";
                    Date saveTime = null;
                    PictureBean pictureBean = dvPlaceBean.getPictureBean();
                    if (pictureBean != null) {
                        List<IdentityCode> identityCodes = dvPlaceBean.getPictureBean().getIdentityCodeList();
                        if (identityCodes.size() > 0) {
                            textA = identityCodes.get(0).getText();
                            if (identityCodes.size() == 2) {
                                textB = identityCodes.get(1).getText();
                            }
                        } else {
                            text = "无数据";
                        }
                        placeCode = dvPlaceBean.getPlaceCode();
                        saveTime = dvPlaceBean.getPictureBean().getSaveTime();
                    }
            %>
            <tr>
                <td><%=placeName%>
                </td>
                <td><%= textA %> &nbsp;&nbsp; <%=textB%> &nbsp;&nbsp;<%=text%>
                </td>
                <td><%=placeCode%>
                </td>
                    <%if (saveTime != null){%>
                        <td> <%=dateFormat.format(saveTime)%> </td>
                   <% }else{%>
                       <td></td>
                   <%} %>
            </tr>
            <% }
            %>
        </table>

    </div>
</div>
</div>
</body>
<script type="text/javascript" src="assets/jquery/1.8.3/jquery.min.js"></script>
<script type="text/javascript" src="assets/bootstrap/3.0.3/js/bootstrap.min.js"></script>

<script type="text/javascript">

    $(function () {
        $(".selectRoute").change(function () {
            var port = this.id;
            var route = this.value;
            $(".route" + port).attr("value", route);
        });
        var getFtpData = function () {
            $.get("ftpConnect.json", function (result) {
                if (result != null) {
                    $("#ftpList").html("");
                    for (var i = 0; i < result.length; i++) {
                        var statue = null;
                        if (result[i].isSuccess != null) {
                            statue = "断开";
                        } else {
                            statue = "正常";
                        }
                        var ftpItems = "<li><strong>" + result[i].ftp + "</strong><ul><li>ftp状态:" + statue + "</li><li>ftp最近一次链接时间" + result[i].connectDate
                                + "</li>";
                        $("#ftpList").append(ftpItems);
                    }
                }
            });
        };

        var ftpData = setInterval(getFtpData, 3600 * 1000);

        var currentState = function () {
            $.get("findCurrentState", function (result) {
                if (result != null) {
                    for (var i in result) {
                        var port = result[i].port;
                        var channelState = result[i].channelState;
                        var state = ""
                        if (channelState) {
                            state = "正常";
                            $("#connectTime" + port).html(result[i].connectTime);
                        } else {
                            state = "异常";
                        }
                        $("#channelState" + port).html(state);
                    }
                }
            });
        };

        var currentState = setInterval(currentState, 10000);

    });

</script>
</html>