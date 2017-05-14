package com.adelmo.raid.domain.fdfs.service;

import com.adelmo.raid.domain.fdfs.vo.Fdfs_file;
import com.adelmo.raid.domain.fdfs.vo.Group;
import com.adelmo.raid.domain.fdfs.vo.GroupDay;
import com.adelmo.raid.domain.fdfs.vo.Line;
import com.adelmo.raid.domain.fdfs.vo.Storage;
import com.adelmo.raid.domain.fdfs.vo.StorageHour;
import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.util.List;
import org.csource.common.MyException;

public interface MonitorService {
    List<Group> listGroupInfo() throws IOException, MyException, JSchException;

    List<Group> listGroups() throws IOException, MyException;

    List<Storage> listStorage(String var1) throws IOException, MyException;

    List<Storage> listStorageTop(String var1) throws IOException, MyException;

    List<Line> listStorageLines(String var1) throws IOException, MyException;

    List<Line> getNetTrafficLines(String var1, String var2, String var3);

    Line getListStoragesInfo(String var1, String var2, String var3) throws IOException, MyException;

    StorageHour getStorageByIp(String var1) throws IOException, MyException;

    List<Group> getAllGroups() throws IOException, MyException;

    List<Line> getListFileCountStorage(String var1, String var2, String var3) throws IOException, MyException;

    void saveFile(Fdfs_file var1) throws IOException, MyException;

    List<GroupDay> getGroupsByName(String var1) throws IOException, MyException;
}
