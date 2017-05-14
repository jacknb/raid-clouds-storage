package com.adelmo.raid.domain.fdfs.service;

import com.adelmo.raid.domain.fdfs.vo.User;
import java.io.IOException;
import java.util.List;
import org.csource.common.MyException;

public interface UserService {
    List<User> userlist(String var1) throws IOException, MyException;

    void updateOrSaveUser(User var1) throws IOException, MyException;

    User findById(String var1) throws IOException, MyException;

    void delUser(String var1) throws IOException, MyException;

    boolean login(String var1, String var2) throws IOException, MyException;

    User findByName(String var1) throws IOException, MyException;
}
