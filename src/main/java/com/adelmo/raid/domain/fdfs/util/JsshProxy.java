package com.adelmo.raid.domain.fdfs.util;

import ch.ethz.ssh2.StreamGobbler;
import com.adelmo.raid.domain.fdfs.util.CommandMessage.MessageType;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class JsshProxy {
    public static String ClientPath = "/usr/local/vagent/";
    public static String ClientShellPath = "/usr/local/vagent/bin/";
    public static String ClientLibPath = "/usr/local/vagent/lib/";
    public static String ClientConfPath = "/usr/local/vagent/conf/";
    public static String ClientLogPath = "/usr/local/vagent/log/";
    public static String ClientWarPath = "/usr/local/vagent/war/";
    private int authType;
    private String ip;
    private String username;
    private String passwd;
    private int port;
    private String dsaPath;
    private JSch jsch = new JSch();

    public JsshProxy(String ip, String username, String passwd, int port) {
        this.ip = ip;
        this.username = username;
        this.passwd = passwd;
        this.port = port;
        this.authType = 1;
    }

    public JsshProxy(String ip, String username, int port, String dsaPath) throws JSchException {
        this.ip = ip;
        this.username = username;
        this.port = port;
        this.dsaPath = dsaPath;
        this.jsch.addIdentity(dsaPath);
        this.authType = 2;
    }

    public CommandMessage updateFile(FileType type, File shellFile) {
        CommandMessage cm = new CommandMessage();
        cm.setCmdString("upload " + shellFile.getName());
        long start = System.currentTimeMillis();
        Session session = null;
        ChannelSftp c = null;
        if(shellFile != null && shellFile.canRead() && shellFile.isFile()) {
            try {
                session = this.jsch.getSession(this.username, this.ip, this.port);
                if(this.authType == 1) {
                    session.setPassword(this.passwd);
                }

//                session.setUserInfo(new JsshProxy.SftpUserInfo((JsshProxy.SyntheticClass_1)null));
                session.setUserInfo(new SftpUserInfo());
                Properties e = new Properties();
                e.put("StrictHostKeyChecking", "no");
                session.setConfig(e);
                cm.setExecuteDate(System.currentTimeMillis());
                session.connect();
                c = (ChannelSftp)session.openChannel("sftp");
                c.connect();
                String distpath = "";
                switch(type.ordinal()) {
                case 1:
                    distpath = ClientShellPath;
                    break;
                case 2:
                    distpath = ClientConfPath;
                    break;
                case 3:
                    distpath = ClientLibPath;
                    break;
                case 4:
                    distpath = ClientLogPath;
                    break;
                case 5:
                    distpath = ClientWarPath;
                }

                try {
                    c.mkdir(ClientPath);
                } catch (SftpException var18) {
                    ;
                }

                try {
                    c.mkdir(distpath);
                } catch (SftpException var17) {
                    ;
                }

                c.put(shellFile.getAbsolutePath(), distpath + shellFile.getName(), 0);
                if(type == FileType.SHELL) {
                    c.chmod(755, distpath + shellFile.getName());
                }

                cm.setType(MessageType.OK);
                cm.setMessage("更新文件 ( " + shellFile.getAbsolutePath() + " ) 成功");
            } catch (JSchException var19) {
                cm.setType(MessageType.CLIENT_ERROR);
                cm.setMessage(var19.getMessage());
                var19.printStackTrace();
            } catch (SftpException var20) {
                cm.setType(MessageType.CLIENT_ERROR);
                cm.setMessage(var20.getMessage());
                var20.printStackTrace();
            } finally {
                if(c != null) {
                    c.disconnect();
                    c.exit();
                }

                if(session != null) {
                    session.disconnect();
                }

                cm.setExecuteTime(System.currentTimeMillis() - start);
            }
        } else {
            cm.setType(MessageType.ERROR);
            cm.setMessage("File error:" + shellFile);
        }

        return cm;
    }

    public CommandMessage execute(String shellName) {
        CommandMessage cm = new CommandMessage();
        cm.setCmdString(shellName);
        long start = System.currentTimeMillis();
        Session session = null;
        ChannelExec c = null;
        InputStream in = null;

        try {
            session = this.jsch.getSession(this.username, this.ip, this.port);
            if(this.authType == 1) {
                session.setPassword(this.passwd);
            }

            session.setUserInfo(new SftpUserInfo());
            Properties e = new Properties();
            e.put("StrictHostKeyChecking", "no");
            session.setConfig(e);
            cm.setExecuteDate(System.currentTimeMillis());
            session.connect();
            c = (ChannelExec)session.openChannel("exec");
            c.setCommand(shellName);
            c.setInputStream((InputStream)null);
            in = c.getInputStream();
            c.connect();
            StreamGobbler is = new StreamGobbler(c.getInputStream());
            BufferedReader brs = new BufferedReader(new InputStreamReader(is));

            for(String line = brs.readLine(); line != null; line = brs.readLine()) {
                cm.getExecuteLines().add(line);
            }

            cm.setType(MessageType.OK);
        } catch (JSchException var22) {
            cm.setType(MessageType.CLIENT_ERROR);
            cm.setMessage(var22.getMessage());
        } catch (IOException var23) {
            cm.setType(MessageType.CLIENT_ERROR);
            cm.setMessage(var23.getMessage());
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException var21) {
                    var21.printStackTrace();
                }
            }

            if(c != null) {
                c.disconnect();
            }

            if(session != null) {
                session.disconnect();
            }

            cm.setExecuteTime(System.currentTimeMillis() - start);
        }

        return cm;
    }

    public CommandMessage executeCommand(String cmdString, List<String> par) {
        CommandMessage cm = new CommandMessage();
        cm.setCmdString(cmdString);
        long start = System.currentTimeMillis();
        Session session = null;
        ChannelExec c = null;
        InputStream in = null;
        ByteArrayOutputStream baos = null;

        try {
            session = this.jsch.getSession(this.username, this.ip, this.port);
            if(this.authType == 1) {
                session.setPassword(this.passwd);
            }

            session.setUserInfo(new SftpUserInfo());
            Properties e = new Properties();
            e.put("StrictHostKeyChecking", "no");
            session.setConfig(e);
            cm.setExecuteDate(System.currentTimeMillis());
            session.connect(1000);
            c = (ChannelExec)session.openChannel("exec");
            c.setCommand(cmdString);
            baos = new ByteArrayOutputStream();
            c.setOutputStream(baos);
            c.setErrStream(baos);
            in = c.getInputStream();
            StringBuilder sb = new StringBuilder();
            c.connect();
            String message;
            if(par != null) {
                Iterator tmp = par.iterator();

                while(tmp.hasNext()) {
                    message = (String)tmp.next();
                    c.getOutputStream().write((message + "\n").getBytes());
                    c.getOutputStream().flush();

                    try {
                        Thread.sleep(500L);
                    } catch (Exception var33) {
                        ;
                    }
                }
            }

            byte[] tmp1 = new byte[1024];

            while(true) {
                while(true) {
                    if(in.available() > 0) {
                        int message1 = in.read(tmp1, 0, 1024);
                        if(message1 >= 0) {
                            sb.append(new String(tmp1, 0, message1));
                            continue;
                        }
                    }

                    if(c.isClosed()) {
                        message = baos.toString("utf-8");
                        if(message.length() > 0) {
                            cm.setType(MessageType.CLIENT_ERROR);
                            cm.setMessage(message);
                        } else {
                            cm.setType(MessageType.OK);
                            cm.setMessage(sb.toString());
                        }

                        return cm;
                    }

                    try {
                        Thread.sleep(200L);
                    } catch (Exception var32) {
                        ;
                    }
                }
            }
        } catch (JSchException var34) {
            cm.setType(MessageType.CLIENT_ERROR);
            cm.setMessage(var34.getMessage());
        } catch (IOException var35) {
            cm.setType(MessageType.CLIENT_ERROR);
            cm.setMessage(var35.getMessage());
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException var31) {
                    var31.printStackTrace();
                }
            }

            if(baos != null) {
                try {
                    baos.close();
                } catch (IOException var30) {
                    var30.printStackTrace();
                }
            }

            if(c != null) {
                c.disconnect();
            }

            if(session != null) {
                session.disconnect();
            }

            cm.setExecuteTime(System.currentTimeMillis() - start);
        }

        return cm;
    }

    public CommandMessage updatePasswd(String newPasswd) {
        CommandMessage cm;
        if(this.authType == 1) {
            cm = this.executeCommand("echo " + this.username + ":" + this.passwd + " | chpasswd", (List)null);
            this.passwd = newPasswd;
        } else {
            cm = new CommandMessage();
            cm.setExecuteDate(System.currentTimeMillis());
            cm.setCmdString("passwd");
            cm.setMessage("登录类型位RSA登录");
            cm.setType(MessageType.ERROR);
        }

        return cm;
    }

    public String getIp() {
        return this.ip;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public int getPort() {
        return this.port;
    }

    private static class SftpUserInfo implements UserInfo, UIKeyboardInteractive {
        String passwd;

        private SftpUserInfo() {
        }

        public String getPassword() {
            return this.passwd;
        }

        public void setPassword(String passwd) {
            this.passwd = passwd;
        }

        public boolean promptYesNo(String str) {
            return true;
        }

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            return true;
        }

        public boolean promptPassword(String message) {
            return true;
        }

        public void showMessage(String message) {
        }

        public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
            return null;
        }
    }

    public static enum FileType {
        SHELL,
        CONFIG,
        LIB,
        LOG,
        WAR;

        private FileType() {
        }
    }
}
