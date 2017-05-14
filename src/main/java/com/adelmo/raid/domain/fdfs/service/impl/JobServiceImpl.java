package com.adelmo.raid.domain.fdfs.service.impl;

import com.adelmo.raid.domain.fdfs.service.BaseService;
import com.adelmo.raid.domain.fdfs.service.FileDataService;
import com.adelmo.raid.domain.fdfs.service.JobService;
import com.adelmo.raid.domain.fdfs.service.WarningService;
import com.adelmo.raid.domain.fdfs.util.BuildMail;
import com.adelmo.raid.domain.fdfs.util.JsshProxy;
import com.adelmo.raid.domain.fdfs.util.Tools;
import com.adelmo.raid.domain.fdfs.vo.DownloadFileRecord;
import com.adelmo.raid.domain.fdfs.vo.Group;
import com.adelmo.raid.domain.fdfs.vo.GroupDay;
import com.adelmo.raid.domain.fdfs.vo.GroupHour;
import com.adelmo.raid.domain.fdfs.vo.Machine;
import com.adelmo.raid.domain.fdfs.vo.Storage;
import com.adelmo.raid.domain.fdfs.vo.StorageDay;
import com.adelmo.raid.domain.fdfs.vo.StorageHour;
import com.adelmo.raid.domain.fdfs.vo.WarningData;
import com.adelmo.raid.domain.fdfs.vo.WarningUser;
import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StructGroupStat;
import org.csource.fastdfs.StructStorageStat;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobServiceImpl extends BaseService implements JobService {
    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);
    @Autowired
    private WarningService warningService;
    @Autowired
    private FileDataService fileDataService;
    Map<String, Date> datemap = new HashMap();

    public JobServiceImpl() {
    }

    @Scheduled(
        cron = "0 0/1 * * * ?"
    )
    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void updateGroupByMinute() throws IOException, MyException, JSchException {
        logger.info("group minute data upate begin...");
        List groups = this.getGroupInfoByMinute();
        Session session = this.getSession();
        Iterator i$ = groups.iterator();

        while(i$.hasNext()) {
            Group group = (Group)i$.next();
            session.save(group);
        }

        logger.info("group minute data upated end");
    }

    @Scheduled(
        cron = "0 0 0/1 * * ?"
    )
    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void updateGroupByHour() throws IOException, MyException, JSchException {
        logger.info("group hour data upate begin...");
        List groups = this.getGroupInfoByHour();
        Session session = this.getSession();
        Iterator i$ = groups.iterator();

        while(i$.hasNext()) {
            GroupHour group = (GroupHour)i$.next();
            session.save(group);
        }

        logger.info("group hour data upated end");
    }

    @Scheduled(
        cron = "0 0 0 0/1 * ?"
    )
    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void updateGroupByDay() throws IOException, MyException, JSchException {
        logger.info("group day data upate begin...");
        List groups = this.getGroupInfoByDay();
        Session session = this.getSession();
        Iterator i$ = groups.iterator();

        while(i$.hasNext()) {
            GroupDay group = (GroupDay)i$.next();
            session.save(group);
        }

        logger.info("group day data upated end");
    }

    private List<Group> getGroupInfoByMinute() throws IOException, MyException, JSchException {
        ArrayList result = new ArrayList();
        ClientGlobal.init(Tools.getClassPath() + "fdfs_client.conf");
        logger.info("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
        logger.info("charset=" + ClientGlobal.g_charset);
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        if(trackerServer == null) {
            return result;
        } else {
            StructGroupStat[] groupStats = tracker.listGroups(trackerServer);
            if(groupStats == null) {
                logger.error("ERROR! list groups error, error no: " + tracker.getErrorCode());
                return result;
            } else {
                logger.info("group count: " + groupStats.length);
                StructGroupStat[] date = groupStats;
                int cmd = groupStats.length;

                Storage storage;
                for(int i$ = 0; i$ < cmd; ++i$) {
                    StructGroupStat machine = date[i$];
                    Group strList = new Group();
                    BeanUtils.copyProperties(machine, strList);
                    StructStorageStat[] i$1 = tracker.listStorages(trackerServer, machine.getGroupName());
                    StructStorageStat[] str = i$1;
                    int i$2 = i$1.length;

                    for(int group = 0; group < i$2; ++group) {
                        StructStorageStat i$3 = str[group];
                        storage = new Storage();
                        BeanUtils.copyProperties(i$3, storage);
                        storage.setId((String)null);
                        System.out.println("getGroupInfoByMinute: storageId:" + storage.getId());
                        storage.setCurStatus(ProtoCommon.getStorageStatusCaption(i$3.getStatus()));
                        storage.setGroup(strList);
                        storage.setGroupName(strList.getGroupName());
                        strList.getStorageList().add(storage);
                    }

                    result.add(strList);
                }

                Date var17 = new Date();
                String var18 = "ps -aux|grep fdfs";
                Iterator var19 = Tools.machines.iterator();

                label62:
                while(var19.hasNext()) {
                    Machine var20 = (Machine)var19.next();
                    new ArrayList();
                    List var21;
                    if(var20.isConfigType()) {
                        var21 = Tools.exeRemoteConsole(var20.getIp(), var20.getUsername(), var20.getPassword(), var18);
                    } else {
                        var21 = (new JsshProxy(var20.getIp(), var20.getUsername(), var20.getPort(), var20.getSsh())).execute(var18).getExecuteLines();
                    }

                    Iterator var22 = var21.iterator();

                    while(true) {
                        String var23;
                        do {
                            if(!var22.hasNext()) {
                                continue label62;
                            }

                            var23 = (String)var22.next();
                        } while(!var23.contains("storage.conf"));

                        Iterator var24 = result.iterator();

                        while(var24.hasNext()) {
                            Group var25 = (Group)var24.next();
                            var25.setCreated(var17);

                            for(Iterator var26 = var25.getStorageList().iterator(); var26.hasNext(); this.warningOffline(storage)) {
                                storage = (Storage)var26.next();
                                if(var20.getIp().equalsIgnoreCase(storage.getIpAddr())) {
                                    String[] strArrray = var23.replaceAll(" +", ",").split(",");
                                    storage.setCpu(strArrray[2]);
                                    storage.setMem(Float.parseFloat(strArrray[3]));
                                    storage.setCreated(var17);
                                    this.warning(storage);
                                }
                            }
                        }
                    }
                }

                return result;
            }
        }
    }

    private List<GroupHour> getGroupInfoByHour() throws IOException, MyException, JSchException {
        ArrayList result = new ArrayList();
        ClientGlobal.init(Tools.getClassPath() + "fdfs_client.conf");
        logger.info("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
        logger.info("charset=" + ClientGlobal.g_charset);
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        if(trackerServer == null) {
            return result;
        } else {
            StructGroupStat[] groupStats = tracker.listGroups(trackerServer);
            if(groupStats == null) {
                logger.error("ERROR! list groups error, error no: " + tracker.getErrorCode());
                return result;
            } else {
                logger.info("group count: " + groupStats.length);
                StructGroupStat[] date = groupStats;
                int cmd = groupStats.length;

                StorageHour storage;
                for(int i$ = 0; i$ < cmd; ++i$) {
                    StructGroupStat machine = date[i$];
                    GroupHour strList = new GroupHour();
                    BeanUtils.copyProperties(machine, strList);
                    StructStorageStat[] i$1 = tracker.listStorages(trackerServer, machine.getGroupName());
                    StructStorageStat[] str = i$1;
                    int i$2 = i$1.length;

                    for(int group = 0; group < i$2; ++group) {
                        StructStorageStat i$3 = str[group];
                        storage = new StorageHour();
                        BeanUtils.copyProperties(i$3, storage);
                        storage.setCurStatus(ProtoCommon.getStorageStatusCaption(i$3.getStatus()));
                        storage.setId((String)null);
                        System.out.println("getGroupInfoByHour: storageId:" + storage.getId());
                        storage.setGroup(strList);
                        storage.setGroupName(strList.getGroupName());
                        strList.getStorageList().add(storage);
                    }

                    result.add(strList);
                }

                Date var17 = new Date();
                String var18 = "ps -aux|grep fdfs";
                Iterator var19 = Tools.machines.iterator();

                label61:
                while(var19.hasNext()) {
                    Machine var20 = (Machine)var19.next();
                    new ArrayList();
                    List var21;
                    if(var20.isConfigType()) {
                        var21 = Tools.exeRemoteConsole(var20.getIp(), var20.getUsername(), var20.getPassword(), var18);
                    } else {
                        var21 = (new JsshProxy(var20.getIp(), var20.getUsername(), var20.getPort(), var20.getSsh())).execute(var18).getExecuteLines();
                    }

                    Iterator var22 = var21.iterator();

                    while(true) {
                        String var23;
                        do {
                            if(!var22.hasNext()) {
                                continue label61;
                            }

                            var23 = (String)var22.next();
                        } while(!var23.contains("storage.conf"));

                        Iterator var24 = result.iterator();

                        while(var24.hasNext()) {
                            GroupHour var25 = (GroupHour)var24.next();
                            var25.setCreated(var17);
                            Iterator var26 = var25.getStorageList().iterator();

                            while(var26.hasNext()) {
                                storage = (StorageHour)var26.next();
                                if(var20.getIp().equalsIgnoreCase(storage.getIpAddr())) {
                                    String[] strArrray = var23.replaceAll(" +", ",").split(",");
                                    storage.setCpu(strArrray[2]);
                                    storage.setMem(Float.parseFloat(strArrray[3]));
                                    storage.setCreated(var17);
                                }
                            }
                        }
                    }
                }

                return result;
            }
        }
    }

    private List<GroupDay> getGroupInfoByDay() throws IOException, MyException, JSchException {
        ArrayList result = new ArrayList();
        ClientGlobal.init(Tools.getClassPath() + "fdfs_client.conf");
        logger.info("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
        logger.info("charset=" + ClientGlobal.g_charset);
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        if(trackerServer == null) {
            return result;
        } else {
            StructGroupStat[] groupStats = tracker.listGroups(trackerServer);
            if(groupStats == null) {
                logger.error("ERROR! list groups error, error no: " + tracker.getErrorCode());
                return result;
            } else {
                logger.info("group count: " + groupStats.length);
                StructGroupStat[] date = groupStats;
                int cmd = groupStats.length;

                StorageDay storage;
                for(int i$ = 0; i$ < cmd; ++i$) {
                    StructGroupStat machine = date[i$];
                    GroupDay strList = new GroupDay();
                    BeanUtils.copyProperties(machine, strList);
                    StructStorageStat[] i$1 = tracker.listStorages(trackerServer, machine.getGroupName());
                    StructStorageStat[] str = i$1;
                    int i$2 = i$1.length;

                    for(int group = 0; group < i$2; ++group) {
                        StructStorageStat i$3 = str[group];
                        storage = new StorageDay();
                        BeanUtils.copyProperties(i$3, storage);
                        storage.setCurStatus(ProtoCommon.getStorageStatusCaption(i$3.getStatus()));
                        storage.setGroup(strList);
                        storage.setId((String)null);
                        storage.setGroupName(strList.getGroupName());
                        System.out.println("getGroupInfoByDay: storageId:" + storage.getId());
                        strList.getStorageList().add(storage);
                    }

                    result.add(strList);
                }

                Date var17 = new Date();
                String var18 = "ps -aux|grep fdfs";
                Iterator var19 = Tools.machines.iterator();

                label61:
                while(var19.hasNext()) {
                    Machine var20 = (Machine)var19.next();
                    new ArrayList();
                    List var21;
                    if(var20.isConfigType()) {
                        var21 = Tools.exeRemoteConsole(var20.getIp(), var20.getUsername(), var20.getPassword(), var18);
                    } else {
                        var21 = (new JsshProxy(var20.getIp(), var20.getUsername(), var20.getPort(), var20.getSsh())).execute(var18).getExecuteLines();
                    }

                    Iterator var22 = var21.iterator();

                    while(true) {
                        String var23;
                        do {
                            if(!var22.hasNext()) {
                                continue label61;
                            }

                            var23 = (String)var22.next();
                        } while(!var23.contains("storage.conf"));

                        Iterator var24 = result.iterator();

                        while(var24.hasNext()) {
                            GroupDay var25 = (GroupDay)var24.next();
                            var25.setCreated(var17);
                            Iterator var26 = var25.getStorageList().iterator();

                            while(var26.hasNext()) {
                                storage = (StorageDay)var26.next();
                                if(var20.getIp().equalsIgnoreCase(storage.getIpAddr())) {
                                    String[] strArrray = var23.replaceAll(" +", ",").split(",");
                                    storage.setCpu(strArrray[2]);
                                    storage.setMem(Float.parseFloat(strArrray[3]));
                                    storage.setCreated(var17);
                                }
                            }
                        }
                    }
                }

                return result;
            }
        }
    }

    private void warning(Storage storage) throws IOException, MyException {
        List warningDatas = this.warningService.findByIp(storage.getIpAddr());
        StringBuffer stringBuffer = new StringBuffer("异常服务器：" + storage.getIpAddr() + "</br>");
        if(!warningDatas.isEmpty()) {
            float wdCup = Float.parseFloat(((WarningData)warningDatas.get(0)).getWdCpu());
            float wdMem = ((WarningData)warningDatas.get(0)).getWdMem();
            long wdFreeMB = ((WarningData)warningDatas.get(0)).getWdFreeMB();
            boolean res = true;
            if(Float.parseFloat(storage.getCpu()) > wdCup) {
                stringBuffer.append("cpu使用率当前值为： " + storage.getCpu() + "% 大于预警值：" + wdCup + "%</br>");
                res = false;
            }

            if(storage.getMem() > wdMem) {
                stringBuffer.append("内存使用率当前值为： " + storage.getMem() + "% 大于预警值：" + wdMem + "%</br>");
                res = false;
            }

            if(storage.getFreeMB() < wdFreeMB) {
                stringBuffer.append("可用空间当前值为： " + storage.getFreeMB() + "MB 小于预警值：" + wdFreeMB + "MB</br>");
                res = false;
            }

            if(!res) {
                BuildMail buildMail = new BuildMail();
                new ArrayList();
                List warningUser = this.warningService.findWarUser();
                Iterator i$ = warningUser.iterator();

                while(i$.hasNext()) {
                    WarningUser wu = (WarningUser)i$.next();
                    buildMail.sendWarning("VivaMe维我", wu.getEmail(), "dfs预警报告", stringBuffer.toString());
                }
            }
        }

    }

    public void warningOffline(Storage storage) throws IOException, MyException {
        List warningDatas = this.warningService.findByIp(storage.getIpAddr());
        boolean res = false;
        StringBuffer stringBuffer = new StringBuffer("异常服务器 ：" + storage.getIpAddr() + "</br>");
        if(storage.getCurStatus().equals("OFFLINE")) {
            stringBuffer.append("服务器停止工作");
            if(this.datemap.containsKey(storage.getIpAddr())) {
                Date buildMail = (Date)this.datemap.get(storage.getIpAddr());
                Date warningUser = new Date();
                long i$ = warningUser.getTime() - buildMail.getTime();
                if(i$ >= 3600000L) {
                    this.datemap.put(storage.getIpAddr(), new Date());
                    res = true;
                }
            } else {
                this.datemap.put(storage.getIpAddr(), new Date());
                res = true;
            }
        } else if(this.datemap.containsKey(storage.getIpAddr())) {
            this.datemap.remove(storage.getIpAddr());
        }

        if(res) {
            BuildMail buildMail1 = new BuildMail();
            new ArrayList();
            List warningUser1 = this.warningService.findWarUser();
            Iterator i$1 = warningUser1.iterator();

            while(i$1.hasNext()) {
                WarningUser wu = (WarningUser)i$1.next();
                buildMail1.sendWarning("VivaMe维我", wu.getEmail(), "dfs预警报告", stringBuffer.toString());
            }
        }

    }

    @Scheduled(
        cron = "0 0 01 * * ?"
    )
    public void readDataFromLoggerToDataBase() throws JSchException {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(5, -1);
        Date d = c.getTime();
        String date = df.format(d);
        Iterator i$ = Tools.machines.iterator();

        while(i$.hasNext()) {
            Machine machine = (Machine)i$.next();
            String cmd = "cat " + machine.getLogpath() + "/fastdfs_" + date + ".log";
            new ArrayList();
            List strList;
            if(machine.isConfigType()) {
                strList = Tools.exeRemoteConsole(machine.getIp(), machine.getUsername(), machine.getPassword(), cmd);
            } else {
                strList = (new JsshProxy(machine.getIp(), machine.getUsername(), machine.getPort(), machine.getSsh())).execute(cmd).getExecuteLines();
            }

            Iterator i$1 = strList.iterator();

            while(i$1.hasNext()) {
                String str = (String)i$1.next();
                String[] data = str.split(" ");
                if(data[8].equals("200")) {
                    DownloadFileRecord downloadFileRecord = this.fileDataService.getDownloadFileRecordByIpAndFileId(machine.getIp(), data[6]);
                    if(downloadFileRecord != null) {
                        downloadFileRecord.setAccessCount(downloadFileRecord.getAccessCount() + 1L);
                    } else {
                        downloadFileRecord = new DownloadFileRecord();
                        downloadFileRecord.setFileId(data[6]);
                        downloadFileRecord.setSrc_ip(machine.getIp());
                        downloadFileRecord.setAccessCount(1L);
                    }

                    this.fileDataService.saveDF(downloadFileRecord);
                }
            }
        }

    }
}
