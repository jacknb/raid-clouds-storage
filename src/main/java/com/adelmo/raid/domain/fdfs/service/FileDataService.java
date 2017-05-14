package com.adelmo.raid.domain.fdfs.service;

import com.adelmo.raid.domain.fdfs.vo.DownloadFileRecord;
import com.adelmo.raid.domain.fdfs.vo.Fdfs_file;
import java.util.List;

public interface FileDataService {
    List<Fdfs_file> getFileListByGroupName(String var1);

    DownloadFileRecord getDownloadFileRecordByIpAndFileId(String var1, String var2);

    void saveDF(DownloadFileRecord var1);
}
