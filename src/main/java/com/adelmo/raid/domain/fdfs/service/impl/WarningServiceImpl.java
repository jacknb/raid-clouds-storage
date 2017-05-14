package com.adelmo.raid.domain.fdfs.service.impl;

import com.adelmo.raid.domain.fdfs.service.BaseService;
import com.adelmo.raid.domain.fdfs.service.WarningService;
import com.adelmo.raid.domain.fdfs.vo.PageInfo;
import com.adelmo.raid.domain.fdfs.vo.WarningData;
import com.adelmo.raid.domain.fdfs.vo.WarningUser;
import com.mysql.jdbc.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.csource.common.MyException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarningServiceImpl extends BaseService implements WarningService {
    private static final Logger logger = LoggerFactory.getLogger(WarningServiceImpl.class);

    public WarningServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void updateWarning(WarningData wd) throws IOException, MyException {
        Session session = this.getSession();
        session.saveOrUpdate(wd);
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public List<WarningData> findWarning() throws IOException, MyException {
        new ArrayList();
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from WarningData as w");
        Query query = session.createQuery(queryString.toString());
        List warningDatas = query.list();
        return warningDatas;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public List<WarningData> findWarning(WarningData wd, PageInfo pageInfo) throws IOException, MyException {
        new ArrayList();
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from WarningData as wd ");
        if(!StringUtils.isNullOrEmpty(wd.getWdIpAddr())) {
            queryString.append("where wd.wdIpAddr like \'%" + wd.getWdIpAddr() + "%\'");
        }

        Query query = session.createQuery(queryString.toString());
        pageInfo.setTotalCount(query.list().size());
        query.setMaxResults(pageInfo.getNumPerPage());
        query.setFirstResult((pageInfo.getPageNum() - 1) * pageInfo.getNumPerPage());
        List warningDatas = query.list();
        return warningDatas;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public WarningData findById(String id) throws IOException, MyException {
        new WarningData();
        Session session = this.getSession();
        WarningData wd = (WarningData)session.get(WarningData.class, id);
        return wd;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void delWarning(String id) throws IOException, MyException {
        WarningData wd = new WarningData();
        wd.setId(id);
        Session session = this.getSession();
        session.delete(wd);
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public List<WarningData> findByIp(String ip) throws IOException, MyException {
        new ArrayList();
        Session session = this.getSession();
        Query query = session.createQuery("from WarningData wd where wd.wdIpAddr=:ip");
        List warningDatas = query.setString("ip", ip).list();
        return warningDatas;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public List<WarningUser> findWarUser() throws IOException, MyException {
        new ArrayList();
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from WarningUser as w");
        Query query = session.createQuery(queryString.toString());
        List warningUsers = query.list();
        return warningUsers;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public List<WarningUser> findWarUser(WarningUser wu, PageInfo pageInfo) throws IOException, MyException {
        new ArrayList();
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from WarningUser as w ");
        if(!StringUtils.isNullOrEmpty(wu.getName())) {
            queryString.append("where w.name like \'%" + wu.getName() + "%\'");
        }

        Query query = session.createQuery(queryString.toString());
        pageInfo.setTotalCount(query.list().size());
        query.setMaxResults(pageInfo.getNumPerPage());
        query.setFirstResult((pageInfo.getPageNum() - 1) * pageInfo.getNumPerPage());
        List warningUsers = query.list();
        return warningUsers;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public WarningUser findUserId(String id) throws IOException, MyException {
        new WarningUser();
        Session session = this.getSession();
        WarningUser wu = (WarningUser)session.get(WarningUser.class, id);
        return wu;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void delWarUser(String id) throws IOException, MyException {
        WarningUser wu = new WarningUser();
        wu.setId(id);
        Session session = this.getSession();
        session.delete(wu);
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void updateWarUser(WarningUser wu) throws IOException, MyException {
        Session session = this.getSession();
        session.saveOrUpdate(wu);
    }
}
