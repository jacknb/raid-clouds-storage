package com.adelmo.raid.domain.fdfs.service;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import org.csource.common.MyException;

public interface JobService {
    void updateGroupByMinute() throws IOException, MyException, JSchException;

    void updateGroupByHour() throws IOException, MyException, JSchException;

    void updateGroupByDay() throws IOException, MyException, JSchException;

    void readDataFromLoggerToDataBase() throws JSchException;
}
