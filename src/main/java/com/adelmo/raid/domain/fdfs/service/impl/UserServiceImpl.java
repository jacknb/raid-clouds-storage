package com.adelmo.raid.domain.fdfs.service.impl;

import com.adelmo.raid.domain.fdfs.service.BaseService;
import com.adelmo.raid.domain.fdfs.service.UserService;
import com.adelmo.raid.domain.fdfs.vo.User;
import com.adelmo.raid.domain.fdfs.vo.WarningUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.csource.common.MyException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends BaseService implements UserService {
    public UserServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public List<User> userlist(String username) throws IOException, MyException {
        new ArrayList();
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from User as u ");
        if(username != null && username != "") {
            queryString.append("where u.name like \'%" + username + "%\'");
        }

        Query query = session.createQuery(queryString.toString());
        List users = query.list();
        return users;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void updateOrSaveUser(User user) throws IOException, MyException {
        Session session = this.getSession();
        session.saveOrUpdate(user);
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public User findById(String id) throws IOException, MyException {
        new User();
        Session session = this.getSession();
        User u = (User)session.get(WarningUser.class, id);
        return u;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public void delUser(String id) throws IOException, MyException {
        User u = new User();
        u.setId(id);
        Session session = this.getSession();
        session.delete(u);
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public boolean login(String name, String password) throws IOException, MyException {
        new ArrayList();
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from User as u where u.name=:name and u.psword=:password");
        Query query = session.createQuery(queryString.toString());
        List users = query.setParameter("name", name).setParameter("password", password).list();
        boolean res = false;
        if(!users.isEmpty()) {
            res = true;
        }

        return res;
    }

    @Transactional(
        propagation = Propagation.REQUIRED
    )
    public User findByName(String name) throws IOException, MyException {
        User user = null;
        new ArrayList();
        Session session = this.getSession();
        StringBuilder queryString = new StringBuilder("from User as u where u.name=:name ");
        Query query = session.createQuery(queryString.toString());
        List users = query.setParameter("name", name).list();
        if(!users.isEmpty()) {
            user = (User)users.get(0);
        }

        return user;
    }
}
