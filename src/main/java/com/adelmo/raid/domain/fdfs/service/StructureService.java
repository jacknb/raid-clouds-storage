package com.adelmo.raid.domain.fdfs.service;

import com.adelmo.raid.domain.fdfs.vo.Line;
import java.util.List;

public interface StructureService {
    List<Line> listStorageTopLine(String var1);

    List<Line> listStorageAboutFile(String var1);
}
