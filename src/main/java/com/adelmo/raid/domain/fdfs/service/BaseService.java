package com.adelmo.raid.domain.fdfs.service;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService {
    @Autowired
    protected SessionFactory sessionFactory;

    public BaseService() {
    }

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }
}
