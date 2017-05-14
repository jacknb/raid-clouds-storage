package com.adelmo.raid.domain.fdfs.action;

import com.adelmo.raid.domain.fdfs.service.MonitorService;
import com.adelmo.raid.domain.fdfs.service.StructureService;
import com.adelmo.raid.domain.fdfs.util.Tools;
import com.adelmo.raid.domain.fdfs.vo.Group;
import com.adelmo.raid.domain.fdfs.vo.Line;
import com.adelmo.raid.domain.fdfs.vo.Storage;
import com.jcraft.jsch.JSchException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/structure"})
public class StructureAction {
    @Autowired
    private StructureService structureService;
    @Autowired
    private MonitorService monitorService;
    private static final Logger logger = LoggerFactory.getLogger(StructureAction.class);

    public StructureAction() {
    }

    @RequestMapping({"/netStructure"})
    public ModelAndView netStructure() throws JSchException {
        ModelAndView mv = new ModelAndView("structure/netStructure.jsp");

        try {
            mv.addObject("groupInfo", this.monitorService.listGroupInfo());
            mv.addObject("trucker", this.getTrackForStruct());
        } catch (IOException var3) {
            logger.error("", var3);
        } catch (MyException var4) {
            logger.error("", var4);
        }

        return mv;
    }

    @RequestMapping({"/serverInfo"})
    public ModelAndView serverInfo(String ip) throws IOException, MyException, JSchException {
        ModelAndView mv = new ModelAndView("structure/serverInfo.jsp");
        if(ip.indexOf(":") >= 0) {
            String[] groups = ip.split(":");
            ip = groups[0];
        }

        List groups1 = this.monitorService.listGroupInfo();
        Iterator sdf = groups1.iterator();

        while(sdf.hasNext()) {
            Group calendar = (Group)sdf.next();
            Iterator i$ = calendar.getStorageList().iterator();

            while(i$.hasNext()) {
                Storage storage = (Storage)i$.next();
                if(storage.getIpAddr().equals(ip)) {
                    mv.addObject("serverInfo", storage);
                }
            }
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar1 = Calendar.getInstance();
        mv.addObject("end", sdf1.format(calendar1.getTime()));
        calendar1.add(10, -1);
        mv.addObject("start", sdf1.format(calendar1.getTime()));
        return mv;
    }

    @ResponseBody
    @RequestMapping({"/getForperformanceByIp"})
    public List<Line> getForperformanceByIp(String ip) {
        List storageList = this.structureService.listStorageTopLine(ip);
        return storageList;
    }

    @ResponseBody
    @RequestMapping({"/storageInfoForFile"})
    public List<Line> storageInfoForFile(String ip) {
        new ArrayList();
        List storageList = this.structureService.listStorageAboutFile(ip);
        return storageList;
    }

    private String getTrackForStruct() {
        String result = "";

        try {
            ClientGlobal.init(Tools.getClassPath() + "fdfs_client.conf");
        } catch (IOException var22) {
            logger.error("", var22);
        } catch (MyException var23) {
            logger.error("", var23);
        }

        String configFile = Tools.getClassPath() + "fdfs_client.conf";
        FileInputStream fis = null;
        InputStreamReader isr = null;
        Properties p = new Properties();

        try {
            fis = new FileInputStream(configFile);
            isr = new InputStreamReader(fis, "UTF-8");
            p.load(isr);
            fis.close();
            isr.close();
        } catch (Exception var21) {
            var21.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException var20) {
                    logger.error("", var20);
                }
            }

            if(isr != null) {
                try {
                    isr.close();
                } catch (IOException var19) {
                    logger.error("", var19);
                }
            }

        }

        result = p.getProperty("tracker_server");
        return result;
    }
}
