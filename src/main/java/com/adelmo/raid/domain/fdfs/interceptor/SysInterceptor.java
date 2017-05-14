package com.adelmo.raid.domain.fdfs.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SysInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(SysInterceptor.class);
    public static String contentPath = "";

    public SysInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getContextPath();
        String prePath = request.getScheme() + "://" + request.getServerName();
        String basePath = prePath + ":" + request.getServerPort() + path + "/";
        request.setAttribute("basePath", basePath);
        contentPath = path;
        StringBuffer requestpath = request.getRequestURL();
        if(requestpath.indexOf("login") == -1) {
            Object name = request.getSession().getAttribute("username");
            if(name == null) {
                response.sendRedirect(basePath + "/main/login.shtml");
            }
        }

        System.out.println(request.getRemoteAddr() + " " + handler.getClass().getName());
        return true;
    }
}
