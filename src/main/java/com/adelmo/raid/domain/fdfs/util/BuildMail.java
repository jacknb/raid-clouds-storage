package com.adelmo.raid.domain.fdfs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class BuildMail {
    private MimeMessage mimeMsg;
    private Session session;
    private Properties props;
    private String username = "";
    private String password = "";
    private Multipart mp;

    public BuildMail() {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource("application.properties").getFile());
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            this.props = new Properties();

            try {
                this.props.load(fis);
            } catch (IOException var13) {
                var13.printStackTrace();
            }
        } catch (FileNotFoundException var14) {
            var14.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

        this.props.get("mail.smtp.host");
        this.createMimeMessage();
    }

    public boolean createMimeMessage() {
        try {
            System.out.println("准备获取邮件会话对象！");
            this.session = Session.getDefaultInstance(this.props, (Authenticator)null);
        } catch (Exception var3) {
            System.err.println("获取邮件会话对象时发生错误！" + var3);
            return false;
        }

        System.out.println("准备创建MIME邮件对象！");

        try {
            this.mimeMsg = new MimeMessage(this.session);
            this.mp = new MimeMultipart();
            return true;
        } catch (Exception var2) {
            System.err.println("创建MIME邮件对象失败！" + var2);
            return false;
        }
    }

    public void setNeedAuth(boolean need) {
        System.out.println("设置smtp身份认证：mail.smtp.auth = " + need);
        if(this.props == null) {
            this.props = System.getProperties();
        }

        if(need) {
            this.props.put("mail.smtp.auth", "true");
        } else {
            this.props.put("mail.smtp.auth", "false");
        }

    }

    public void setNamePass(String name, String pass) {
        this.username = name;
        this.password = pass;
    }

    public boolean setSubject(String mailSubject) {
        System.out.println("设置邮件主题！");

        try {
            this.mimeMsg.setSubject(mailSubject);
            return true;
        } catch (Exception var3) {
            System.err.println("设置邮件主题发生错误！");
            return false;
        }
    }

    public boolean setBody(String mailBody) {
        try {
            MimeBodyPart e = new MimeBodyPart();
            e.setContent("<meta http-equiv=Content-Type content=text/html; charset=gb2312>" + mailBody, "text/html;charset=GB2312");
            this.mp.addBodyPart(e);
            return true;
        } catch (Exception var3) {
            System.err.println("设置邮件正文时发生错误！" + var3);
            return false;
        }
    }

    public boolean addFileAffix(String filename) {
        System.out.println("增加邮件附件：" + filename);

        try {
            MimeBodyPart e = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(filename);
            e.setDataHandler(new DataHandler(fileds));
            e.setFileName(fileds.getName());
            this.mp.addBodyPart(e);
            return true;
        } catch (Exception var4) {
            System.err.println("增加邮件附件：" + filename + "发生错误！" + var4);
            return false;
        }
    }

    public boolean setFrom(String from, String view) {
        System.out.println("设置发信人！");

        try {
            this.mimeMsg.setFrom(new InternetAddress(from, view, "GB2312"));
            return true;
        } catch (Exception var4) {
            return false;
        }
    }

    public boolean setFromConfig(String view) {
        System.out.println("设置发信人！");

        try {
            this.mimeMsg.setFrom(new InternetAddress(this.props.get("mail.smtp.username").toString(), view));
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public boolean setTo(String to) {
        if(to == null) {
            return false;
        } else {
            try {
                this.mimeMsg.setRecipients(RecipientType.TO, InternetAddress.parse(to));
                return true;
            } catch (Exception var3) {
                return false;
            }
        }
    }

    public boolean setCopyTo(String copyto) {
        if(copyto == null) {
            return false;
        } else {
            try {
                this.mimeMsg.setRecipients(RecipientType.CC, (Address[])InternetAddress.parse(copyto));
                return true;
            } catch (Exception var3) {
                return false;
            }
        }
    }

    public boolean sendout() {
        try {
            this.mimeMsg.setContent(this.mp);
            this.mimeMsg.saveChanges();
            System.out.println("正在发送邮件....");
            Session e = Session.getInstance(this.props, (Authenticator)null);
            Transport transport = e.getTransport("smtp");
            transport.connect((String)this.props.get("mail.smtp.host"), (String)this.props.get("mail.smtp.username"), (String)this.props.get("mail.smtp.password"));
            transport.sendMessage(this.mimeMsg, this.mimeMsg.getRecipients(RecipientType.TO));
            System.out.println("发送邮件成功！");
            transport.close();
            return true;
        } catch (Exception var3) {
            System.err.println("邮件发送失败！" + var3);
            return false;
        }
    }

    public void send(String from, String view, String to, String cc, String title, String body, String object) {
        this.setNeedAuth(true);
        if(this.setSubject(title)) {
            if(this.setBody(body)) {
                if(this.setTo(to)) {
                    if(this.setFrom(from, view)) {
                        if(object == null || this.addFileAffix(object)) {
                            if(this.sendout()) {
                                ;
                            }
                        }
                    }
                }
            }
        }
    }

    public void sendWarning(String view, String to, String title, String body) {
        this.setNeedAuth(true);
        if(this.setSubject(title)) {
            if(this.setBody(body)) {
                if(this.setTo(to)) {
                    if(this.setFromConfig(view)) {
                        if(this.sendout()) {
                            ;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        BuildMail buildMail = new BuildMail();
        buildMail.sendWarning("VivaMe维我", "young_willow@126.com", "VivaMe密码重置", "1234");
    }
}
