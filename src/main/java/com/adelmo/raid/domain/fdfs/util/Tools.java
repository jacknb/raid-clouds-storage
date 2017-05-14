package com.adelmo.raid.domain.fdfs.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.adelmo.raid.domain.fdfs.vo.Machine;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tools {
    private static final Logger logger = LoggerFactory.getLogger(Tools.class);
    public static List<Machine> machines;

    public Tools() {
    }

    public static List<String> exeRemoteConsole(String hostname, String username, String password, String cmd) {
        ArrayList result = new ArrayList();
        Connection conn = new Connection(hostname);
        Session ssh = null;

        try {
            conn.connect();
            boolean e = conn.authenticateWithPassword(username, password);
            if(!e) {
                logger.error("用户名称或者是密码不正确");
            } else {
                logger.info("已经连接OK");
                ssh = conn.openSession();
                ssh.execCommand(cmd);
                StreamGobbler is = new StreamGobbler(ssh.getStdout());
                BufferedReader brs = new BufferedReader(new InputStreamReader(is));

                for(String line = brs.readLine(); line != null; line = brs.readLine()) {
                    result.add(line);
                }
            }

            if(ssh != null) {
                ssh.close();
            }

            conn.close();
        } catch (IOException var11) {
            logger.error("", var11);
        }

        return result;
    }

    public static String getRootPath() {
        String classPath = Tools.class.getClassLoader().getResource("/").getPath();
        String rootPath = "";
        if("\\".equals(File.separator)) {
            rootPath = classPath.substring(1, classPath.indexOf("/WEB-INF/classes"));
            rootPath = rootPath.replace("/", "\\");
        }

        if("/".equals(File.separator)) {
            rootPath = classPath.substring(0, classPath.indexOf("/WEB-INF/classes"));
            rootPath = rootPath.replace("\\", "/");
        }

        return rootPath;
    }

    public static String getClassPath() {
        String classPath = Tools.class.getClassLoader().getResource("/").getPath();
        if("\\".equals(File.separator)) {
            classPath = classPath.replace("/", "\\");
        }

        if("/".equals(File.separator)) {
            classPath = classPath.replace("\\", "/");
        }

        return classPath;
    }

    static {
        SAXReader saxReader = new SAXReader();

        try {
            System.out.println(getClassPath());
            Document e = saxReader.read(getClassPath() + "config.xml");
            Element root = e.getRootElement();
            machines = new ArrayList();
            List elements = root.elements("server");
            Iterator i$ = elements.iterator();

            while(i$.hasNext()) {
                Element element = (Element)i$.next();
                Machine machine = new Machine();
                String ip = element.element("ip").getText();
                String username = element.element("username").getText();
                String logpath;
                if(element.element("password") != null) {
                    logpath = element.element("password").getText();
                    machine.setPassword(logpath);
                    machine.setConfigType(true);
                }

                if(element.element("ssh") != null) {
                    logpath = element.element("ssh").getText();
                    machine.setSsh(logpath);
                    machine.setConfigType(false);
                    int port = Integer.parseInt(element.element("port").getText());
                    machine.setPort(port);
                }

                logpath = element.element("logpath").getText();
                machine.setIp(ip);
                machine.setUsername(username);
                machine.setLogpath(logpath);
                machines.add(machine);
            }
        } catch (DocumentException var11) {
            logger.error("read config.xml error!!!!", var11);
        }

    }
}
