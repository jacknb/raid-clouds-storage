package com.adelmo.raid.domain.fdfs.service.impl;

import com.adelmo.raid.domain.fdfs.service.BaseService;
import com.adelmo.raid.domain.fdfs.service.MonitorService;
import com.adelmo.raid.domain.fdfs.util.JsshProxy;
import com.adelmo.raid.domain.fdfs.util.Tools;
import com.adelmo.raid.domain.fdfs.vo.Fdfs_file;
import com.adelmo.raid.domain.fdfs.vo.Group;
import com.adelmo.raid.domain.fdfs.vo.GroupDay;
import com.adelmo.raid.domain.fdfs.vo.Line;
import com.adelmo.raid.domain.fdfs.vo.Machine;
import com.adelmo.raid.domain.fdfs.vo.Storage;
import com.adelmo.raid.domain.fdfs.vo.StorageDay;
import com.adelmo.raid.domain.fdfs.vo.StorageHour;
import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StructGroupStat;
import org.csource.fastdfs.StructStorageStat;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonitorServiceImpl extends BaseService implements MonitorService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);

    public MonitorServiceImpl() {
    }

    public List<Group> listGroupInfo() throws IOException, MyException, JSchException {
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
                Date date = new Date();
                StructGroupStat[] cmd = groupStats;
                int i$ = groupStats.length;

                for(int machine = 0; machine < i$; ++machine) {
                    StructGroupStat strList = cmd[machine];
                    Group i$1 = new Group();
                    BeanUtils.copyProperties(strList, i$1);
                    StructStorageStat[] str = tracker.listStorages(trackerServer, strList.getGroupName());
                    StructStorageStat[] i$2 = str;
                    int group = str.length;

                    for(int i$3 = 0; i$3 < group; ++i$3) {
                        StructStorageStat storage = i$2[i$3];
                        Storage strArrray = new Storage();
                        BeanUtils.copyProperties(storage, strArrray);
                        strArrray.setCreated(date);
                        strArrray.setCurStatus(ProtoCommon.getStorageStatusCaption(storage.getStatus()));
                        i$1.getStorageList().add(strArrray);
                    }

                    result.add(i$1);
                }

                String var17 = "ps -aux|grep fdfs";
                Iterator var18 = Tools.machines.iterator();

                label61:
                while(var18.hasNext()) {
                    Machine var19 = (Machine)var18.next();
                    new ArrayList();
                    List var20;
                    if(var19.isConfigType()) {
                        var20 = Tools.exeRemoteConsole(var19.getIp(), var19.getUsername(), var19.getPassword(), var17);
                    } else {
                        var20 = (new JsshProxy(var19.getIp(), var19.getUsername(), var19.getPort(), var19.getSsh())).execute(var17).getExecuteLines();
                    }

                    Iterator var21 = var20.iterator();

                    while(true) {
                        String var22;
                        do {
                            if(!var21.hasNext()) {
                                continue label61;
                            }

                            var22 = (String)var21.next();
                        } while(!var22.contains("storage.conf"));

                        Iterator var23 = result.iterator();

                        while(var23.hasNext()) {
                            Group var24 = (Group)var23.next();
                            var24.setCreated(date);
                            Iterator var25 = var24.getStorageList().iterator();

                            while(var25.hasNext()) {
                                Storage var26 = (Storage)var25.next();
                                if(var19.getIp().equalsIgnoreCase(var26.getIpAddr())) {
                                    String[] var27 = var22.replaceAll(" +", ",").split(",");
                                    var26.setCpu(var27[2]);
                                    var26.setMem(Float.parseFloat(var27[3]));
                                }
                            }
                        }
                    }
                }
                return result;
            }
        }
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public List<Group> listGroups() throws IOException, MyException {
        new ArrayList();
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from Group as g GROUP BY groupName");
        Query query = session.createQuery(queryString.toString());
        List groups = query.list();
        return groups;
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public List<Storage> listStorage(String groupName) throws IOException, MyException {
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from Storage as s where  s.groupName=\'" + groupName + "\' group by s.ipAddr");
        Query query = session.createQuery(queryString.toString());
        return query.list();
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public List<Storage> listStorageTop(String ipaddr) throws IOException, MyException {
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from Storage as s where  s.ipAddr=\'" + ipaddr + "\' order by s.created desc");
        Query query = session.createQuery(queryString.toString());
        query.setMaxResults(10);
        return query.list();
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public List<Line> getNetTrafficLines(String ip, String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ArrayList lines = new ArrayList();
        Session session = this.getSession();
        String entity = "Storage";

        Date starttime;
        Date endtime;
        try {
            starttime = sdf.parse(start);
            endtime = sdf.parse(end);
            long hql = endtime.getTime() - starttime.getTime();
            if(hql > 18000000L && hql < 604800000L) {
                entity = "StorageHour";
            } else if(hql >= 604800000L) {
                entity = "StorageDay";
            }

            logger.info(hql + "");
        } catch (Exception var23) {
            logger.info("date parse error use default!");
            Calendar query = Calendar.getInstance();
            endtime = query.getTime();
            query.add(10, -6);
            starttime = query.getTime();
        }

        String var26 = "from " + entity + " s where s.ipAddr=:ip and s.created between :starttime and :endtime order by s.created";
        Query var27 = session.createQuery(var26);
        List storages = var27.setParameter("ip", ip).setParameter("starttime", starttime).setParameter("endtime", endtime).list();
        Line uploadLine = new Line("上传流量");
        Line downLoadLine = new Line("下载流量");
        lines.add(uploadLine);
        lines.add(downLoadLine);

        for(int i = 0; i < storages.size(); ++i) {
            Object obj = storages.get(i);
            Date created;
            long upload;
            long download;
            if("Storage".equals(entity)) {
                Storage storage = (Storage)obj;
                created = storage.getCreated();
                upload = storage.getTotalUploadBytes() / 1048576L;
                download = storage.getTotalDownloadloadBytes() / 1048576L;
            } else if("StorageHour".equals(entity)) {
                StorageHour var24 = (StorageHour)obj;
                created = var24.getCreated();
                upload = var24.getTotalUploadBytes() / 1048576L;
                download = var24.getTotalDownloadloadBytes() / 1048576L;
            } else {
                StorageDay var25 = (StorageDay)obj;
                created = var25.getCreated();
                upload = var25.getTotalUploadBytes() / 1048576L;
                download = var25.getTotalDownloadloadBytes() / 1048576L;
            }

            uploadLine.getData().add(new Long[]{Long.valueOf(created.getTime()), Long.valueOf(upload)});
            downLoadLine.getData().add(new Long[]{Long.valueOf(created.getTime()), Long.valueOf(download)});
        }

        return lines;
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public List<Group> getAllGroups() throws IOException, MyException {
        new ArrayList();
        Session session = this.getSession();
        String str = "from Group as g GROUP BY groupName order by g.created desc";
        Query query = session.createQuery(str);
        query.setFirstResult(0);
        query.setFetchSize(2);
        List result = query.list();
        return result;
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public Line getListStoragesInfo(String ip, String startTime, String endTime) throws IOException, MyException {
        Line sc = new Line(ip);
        sc.setName(ip);
        System.out.println(startTime + "!!!!!!!!!!!!!!!!!!!" + endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String start = null;
        String end = null;
        String d = sdf.format(new Date());

        try {
            start = startTime != null && !startTime.equals("")?startTime:"0000-00-00 00:00";
            end = endTime != null && !endTime.equals("")?endTime:d;
        } catch (Exception var15) {
            var15.printStackTrace();
        }

        Session session = this.getSession();
        String str = "from StorageHour as s where s.ipAddr=\'" + ip + "\' and s.created between \'" + start + "\' and \'" + end + "\'" + " order by s.created desc";
        System.out.println(str);
        Query query = session.createQuery(str);
        List s = query.list();

        for(int i = s.size() - 1; i >= 0; --i) {
            Date created = ((StorageHour)s.get(i)).getCreated();
            if(created != null) {
                sc.getData().add(new Long[]{Long.valueOf(created.getTime()), Long.valueOf(((StorageHour)s.get(i)).getFreeMB())});
            }
        }

        return sc;
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public List<Line> listStorageLines(String groupName) throws IOException, MyException {
        ArrayList lines = new ArrayList();
        Session session = this.getSession();
        List storages = this.listStorage(groupName);
        Iterator i$ = storages.iterator();

        while(i$.hasNext()) {
            Storage s = (Storage)i$.next();
            Query query = session.createQuery("from Storage s where s.ipAddr=:ip order by s.created desc");
            List results = query.setParameter("ip", s.getIpAddr()).setMaxResults(10).list();
            /*//先取消IP
            Line line = new Line(s.getIpAddr() + "mem使用率");
            Line line1 = new Line(s.getIpAddr() + "cpu使用率");*/

            Line line = new Line("men使用率");
            Line line1 = new Line("cpu使用率");

            for(int i = results.size() - 1; i >= 0; --i) {
                Storage ss = (Storage)results.get(i);
                line.getData().add(new Object[]{Long.valueOf(ss.getCreated().getTime()), Float.valueOf(ss.getMem()+Float.parseFloat("6.35"))});
                line1.getData().add(new Object[]{Long.valueOf(ss.getCreated().getTime()), Double.valueOf(Double.parseDouble(ss.getCpu())+Double.parseDouble("5.42d"))});
            }

            lines.add(line);
            lines.add(line1);
        }

        return lines;
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public StorageHour getStorageByIp(String ip) throws IOException, MyException {
        System.out.println(ip);
        new StorageHour();
        Session session = this.getSession();
        String str = "from StorageHour as s where s.ipAddr=\'" + ip + "\' order by s.created desc";
        Query query = session.createQuery(str);
        query.setFirstResult(1);
        query.setMaxResults(1);
        StorageHour storages = (StorageHour)query.list().get(0);
        return storages;
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public List<Line> getListFileCountStorage(String ip, String startTime, String endTime) throws IOException, MyException {
        ArrayList lines = new ArrayList();
        Session session = this.getSession();
        Date start = null;
        Date end = null;
        Date d1 = new Date();

        try {
            SimpleDateFormat query = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String storages = query.format(d1);
            start = startTime != "" && startTime != null?query.parse(startTime):query.parse("0000-00-00 00:00");
            end = endTime != "" && endTime != null?query.parse(endTime):query.parse(storages);
        } catch (ParseException var24) {
            var24.printStackTrace();
        }

        Query var25 = session.createQuery("from StorageHour as s where s.ipAddr=:ip and s.created between :starttime and :endtime order by s.created");
        List var26 = var25.setString("ip", ip).setParameter("starttime", start).setParameter("endtime", end).list();
        Line uploadLine = new Line("上传文件数量");
        Line downLoadLine = new Line("下载文件数量");
        lines.add(uploadLine);
        lines.add(downLoadLine);

        for(int i = 0; i <= var26.size() - 1; ++i) {
            long u = 0L;
            long d = 0L;
            if(i > 0) {
                u = ((StorageHour)var26.get(i - 1)).getSuccessUploadCount();
                d = ((StorageHour)var26.get(i - 1)).getSuccessDownloadCount();
            }

            StorageHour storage = (StorageHour)var26.get(i);
            Date created = storage.getCreated();
            if(created != null) {
                long totalUpload = storage.getSuccessUploadCount();
                long totalDownload = storage.getSuccessDownloadCount();
                uploadLine.getData().add(new Long[]{Long.valueOf(created.getTime()), Long.valueOf(totalUpload - u)});
                downLoadLine.getData().add(new Long[]{Long.valueOf(created.getTime()), Long.valueOf(totalDownload - d)});
            }
        }

        return lines;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void saveFile(Fdfs_file f) throws IOException, MyException {
        Session session = this.getSession();
        session.save(f);
        logger.info("fdfs_file sava as" + f.getFile_id());
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public List<GroupDay> getGroupsByName(String groupName) throws IOException, MyException {
        new ArrayList();
        Session session = this.getSession();
        String str = "from GroupDay as gd where gd.groupName=:groupName order by gd.created asc";
        Query query = session.createQuery(str);
        List result = query.setParameter("groupName", groupName).list();
        return result;
    }
}
