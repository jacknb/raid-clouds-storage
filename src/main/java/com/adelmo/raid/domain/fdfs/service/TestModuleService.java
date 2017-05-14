package com.adelmo.raid.domain.fdfs.service;

import com.adelmo.raid.domain.fdfs.vo.Fdfs_file;
import com.adelmo.raid.domain.fdfs.vo.Line;
import java.util.List;
import java.util.Map;

public interface TestModuleService {
    List<Fdfs_file> getAllFileList();

    Fdfs_file getFileByFileId(String var1);

    void saveFastFile(Fdfs_file var1);

    Map<String, Object[]> getAllFileListByTen(String var1);

    Line getAllFileListForPie(String var1);

    List<Fdfs_file> getAllFileListByPage(String var1, String var2, String var3);

    int getCountDownLoadFile(String var1);
}
