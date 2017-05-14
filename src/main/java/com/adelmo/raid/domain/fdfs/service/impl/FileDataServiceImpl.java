package com.adelmo.raid.domain.fdfs.service.impl;

import com.adelmo.raid.domain.fdfs.service.BaseService;
import com.adelmo.raid.domain.fdfs.service.FileDataService;
import com.adelmo.raid.domain.fdfs.vo.DownloadFileRecord;
import com.adelmo.raid.domain.fdfs.vo.Fdfs_file;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileDataServiceImpl extends BaseService implements FileDataService {
    public FileDataServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public List<Fdfs_file> getFileListByGroupName(String groupName) {
        new ArrayList();
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from Fdfs_file as f where  f.file_id like \'" + groupName + "%\'");
        Query query = session.createQuery(queryString.toString());
        List files = query.list();
        return files;
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public DownloadFileRecord getDownloadFileRecordByIpAndFileId(String ip, String fileId) {
        new DownloadFileRecord();
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("  from DownloadFileRecord df where df.src_ip=\'" + ip + "\' and df.fileId=\'" + fileId + "\'");
        Query query = session.createQuery(queryString.toString());
        List list = query.list();
        return list.isEmpty()?null:(DownloadFileRecord)list.get(0);
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void saveDF(DownloadFileRecord downloadFileRecord) {
        Session session = this.getSession();
        session.saveOrUpdate(downloadFileRecord);
    }
}
