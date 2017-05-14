package com.adelmo.raid.domain.fdfs.service;

import com.adelmo.raid.domain.fdfs.vo.PageInfo;
import com.adelmo.raid.domain.fdfs.vo.WarningData;
import com.adelmo.raid.domain.fdfs.vo.WarningUser;
import java.io.IOException;
import java.util.List;
import org.csource.common.MyException;

public interface WarningService {
    void updateWarning(WarningData var1) throws IOException, MyException;

    List<WarningData> findWarning() throws IOException, MyException;

    List<WarningData> findWarning(WarningData var1, PageInfo var2) throws IOException, MyException;

    WarningData findById(String var1) throws IOException, MyException;

    void delWarning(String var1) throws IOException, MyException;

    List<WarningData> findByIp(String var1) throws IOException, MyException;

    List<WarningUser> findWarUser() throws IOException, MyException;

    List<WarningUser> findWarUser(WarningUser var1, PageInfo var2) throws IOException, MyException;

    WarningUser findUserId(String var1) throws IOException, MyException;

    void delWarUser(String var1) throws IOException, MyException;

    void updateWarUser(WarningUser var1) throws IOException, MyException;
}
