package com.microwise.proxima.service.impl;

import com.microwise.proxima.bean.FTPProfile;
import com.microwise.proxima.dao.FTPProfileDao;
import com.microwise.proxima.service.FTPProfileService;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ftp配置Service实现类
 *
 * @author wnagyunlong
 * @date 2013-3-26
 */
@Service
@Scope("prototype")
@Transactional
public class FTPProfileServiceImpl implements FTPProfileService {

    /**
     * ftp配置Dao层
     */
    @Autowired
    private FTPProfileDao ftpProfileDao;

    @Override
    public void save(FTPProfile ftp) {
        ftpProfileDao.save(ftp);
    }

    @Override
    public void update(FTPProfile ftp) {
        ftpProfileDao.update(ftp);
    }

    @Override
    public FTPProfile findById(String ftpId) {
        return ftpProfileDao.findById(ftpId);
    }

    @Override
    public List<FTPProfile> findAllFtp() {
        return ftpProfileDao.findAll();
    }


    @Override
    public Map<String, Object> connectFtp(FTPProfile ftp) {
        FTPClient client = new FTPClient();
        String CONNECT_EXCEPTION = "FTP服务器无法访问";
        String LOGIN_EXCEPTION = "用户名或密码不正确";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ftp", ftp.getName());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("connectDate", dateFormat.format(new Date()));
        // 连接
        try {
            client.connect(ftp.getHost(), ftp.getPort());
        } catch (IllegalStateException e) {
            map.put("isSuccess", CONNECT_EXCEPTION);
            return map;
        } catch (IOException e) {
            map.put("isSuccess", CONNECT_EXCEPTION);
            return map;
        } catch (FTPIllegalReplyException e) {
            map.put("isSuccess", CONNECT_EXCEPTION);
            return map;
        } catch (FTPException e) {
            map.put("isSuccess", CONNECT_EXCEPTION);
            return map;
        }
        // 登陆
        try {
            client.login(ftp.getUsername(), ftp.getPassword());
        } catch (IllegalStateException e) {
            map.put("isSuccess", LOGIN_EXCEPTION);
            return map;
        } catch (IOException e) {
            map.put("isSuccess", LOGIN_EXCEPTION);
            return map;
        } catch (FTPIllegalReplyException e) {
            map.put("isSuccess", LOGIN_EXCEPTION);
            return map;
        } catch (FTPException e) {
            map.put("isSuccess", LOGIN_EXCEPTION);
            return map;
        } finally {
            try {
                client.disconnect(true);
                // 此处故意不处理, 退出失败不影响结果
            } catch (IllegalStateException | IOException | FTPException | FTPIllegalReplyException ignored) {
            }
        }
        map.put("isSuccess", null);
        return map;
    }

}
