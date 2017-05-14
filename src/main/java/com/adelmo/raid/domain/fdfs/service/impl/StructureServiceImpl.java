package com.adelmo.raid.domain.fdfs.service.impl;

import com.adelmo.raid.domain.fdfs.service.BaseService;
import com.adelmo.raid.domain.fdfs.service.StructureService;
import com.adelmo.raid.domain.fdfs.vo.Line;
import com.adelmo.raid.domain.fdfs.vo.Storage;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StructureServiceImpl extends BaseService implements StructureService {
    public StructureServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public List<Line> listStorageTopLine(String ip) {
        ArrayList lines = new ArrayList();
        Session session = this.getSession();
        Query query = session.createQuery("from Storage s where s.ipAddr=:ip order by s.created desc");
        List results = query.setString("ip", ip).setMaxResults(10).list();
        Line line = new Line(ip);

        for(int i = results.size() - 1; i >= 0; --i) {
            Storage ss = (Storage)results.get(i);
            line.getData().add(new Object[]{Long.valueOf(ss.getCreated().getTime()), Float.valueOf(ss.getMem())});
        }

        lines.add(line);
        return lines;
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true
    )
    public List<Line> listStorageAboutFile(String ip) {
        ArrayList lines = new ArrayList();
        Session session = this.getSession();
        Query query = session.createQuery("from Storage s where s.ipAddr=:ip order by s.created desc");
        List results = query.setString("ip", ip).setMaxResults(10).list();
        Line line = new Line(ip);

        for(int line1 = results.size() - 1; line1 >= 0; --line1) {
            Storage i = (Storage)results.get(line1);
            line.getData().add(new Object[]{Long.valueOf(i.getCreated().getTime()), Long.valueOf(i.getTotalDownloadCount())});
        }

        lines.add(line);
        Line var10 = new Line(ip);

        for(int var11 = results.size() - 1; var11 >= 0; --var11) {
            Storage ss = (Storage)results.get(var11);
            var10.getData().add(new Object[]{Long.valueOf(ss.getCreated().getTime()), Long.valueOf(ss.getTotalUploadCount())});
        }

        lines.add(var10);
        return lines;
    }
}
