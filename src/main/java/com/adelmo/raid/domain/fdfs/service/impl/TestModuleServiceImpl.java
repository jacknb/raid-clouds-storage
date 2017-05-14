package com.adelmo.raid.domain.fdfs.service.impl;

import com.adelmo.raid.domain.fdfs.service.BaseService;
import com.adelmo.raid.domain.fdfs.service.TestModuleService;
import com.adelmo.raid.domain.fdfs.vo.DownloadFileRecord;
import com.adelmo.raid.domain.fdfs.vo.Fdfs_file;
import com.adelmo.raid.domain.fdfs.vo.Line;
import com.mysql.jdbc.StringUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestModuleServiceImpl extends BaseService implements TestModuleService {
    public TestModuleServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public List<Fdfs_file> getAllFileList() {
        Session session = this.getSession();
        Query query = session.createQuery(" from Fdfs_file");
        return query.list();
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public Fdfs_file getFileByFileId(String fileId) {
        Session session = this.getSession();
        Query query = session.createQuery(" from Fdfs_file f where f.file_id=\'" + fileId + "\'");
        List fileList = query.list();
        return fileList.size() > 0?(Fdfs_file)fileList.get(0):null;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void saveFastFile(Fdfs_file f) {
        Session session = this.getSession();
        session.saveOrUpdate(f);
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public Map<String, Object[]> getAllFileListByTen(String ip) {
        HashMap map = new HashMap();
        Session session = this.getSession();
        Query query = session.createQuery(" from DownloadFileRecord  f where f.src_ip=\'" + ip + "\'  order by f.accessCount desc");
        List list = query.list();
        Line sc = new Line(ip);
        sc.setName(ip);
        String[] listName = new String[10];
        Line[] lines = new Line[1];
        long sum = 0L;

        for(int i = 0; i < list.size(); ++i) {
            DownloadFileRecord downloadFileRecord = (DownloadFileRecord)list.get(i);
            if(i < 10) {
                Fdfs_file f = this.getFileByFileId(downloadFileRecord.getFileId().substring(1));
                sc.getData().add(new Object[]{f.getFile_name(), Long.valueOf(downloadFileRecord.getAccessCount())});
                listName[i] = f.getFile_name();
                sum += downloadFileRecord.getAccessCount();
            } else {
                sum += downloadFileRecord.getAccessCount();
            }
        }

        lines[0] = sc;
        map.put("x", listName);
        map.put("y", lines);
        map.put("sum", new Object[]{Long.valueOf(sum)});
        return map;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public Line getAllFileListForPie(String ip) {
        Session session = this.getSession();
        Query query = session.createQuery(" from DownloadFileRecord  f where f.src_ip=\'" + ip + "\'  order by f.accessCount desc");
        query.setMaxResults(10);
        List list = query.list();
        Line sc = new Line(ip);
        sc.setName(ip);
        Iterator i$ = list.iterator();

        while(i$.hasNext()) {
            DownloadFileRecord downloadFileRecord = (DownloadFileRecord)i$.next();
            Fdfs_file f = this.getFileByFileId(downloadFileRecord.getFileId().substring(1));
            sc.getData().add(new Object[]{f.getFile_name(), Long.valueOf(downloadFileRecord.getAccessCount())});
        }

        return sc;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public List<Fdfs_file> getAllFileListByPage(String pageNum, String pageSize, String keyForSearch) {
        Session session = this.getSession();
        StringBuilder sb = new StringBuilder(" from Fdfs_file f");
        if(!StringUtils.isNullOrEmpty(keyForSearch)) {
            sb.append("  where f.file_id=\'" + keyForSearch + "\'");
            pageNum = "1";
        }

        Query query = session.createQuery(sb.toString());
        query.setMaxResults(Integer.parseInt(pageSize));
        query.setFirstResult((Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize));
        return query.list();
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public int getCountDownLoadFile(String keyForSearch) {
        Session session = this.getSession();
        StringBuilder sb = new StringBuilder(" from Fdfs_file f");
        if(keyForSearch != null && keyForSearch != "") {
            sb.append("  where f.file_id=\'" + keyForSearch + "\'");
        }

        Query query = session.createQuery(sb.toString());
        return query.list().size();
    }
}
