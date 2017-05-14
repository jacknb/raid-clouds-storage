//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adelmo.raid.domain.fdfs.action;

import com.adelmo.raid.domain.fdfs.service.MonitorService;
import com.adelmo.raid.domain.fdfs.service.TestModuleService;
import com.adelmo.raid.domain.fdfs.util.Tools;
import com.adelmo.raid.domain.fdfs.vo.Fdfs_file;
import com.adelmo.raid.domain.fdfs.vo.Line;
import com.adelmo.raid.domain.fdfs.vo.Message;
import com.jcraft.jsch.JSchException;
import com.mysql.jdbc.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/testModule"})
public class TestModuleAction {
    @Autowired
    private TestModuleService testModuleService;
    @Autowired
    private MonitorService monitorService;
    private static final Logger logger = LoggerFactory.getLogger(TestModuleAction.class);

    public TestModuleAction() {
    }

    @RequestMapping({"/testDownLoad"})
    public ModelAndView testDownLoad(String pageNum, String pageSize, String keyForSearch) {
        ModelAndView mv = new ModelAndView("testModule/downLoadTest.jsp");
        List list = this.testModuleService.getAllFileListByPage(pageNum, pageSize, keyForSearch);
        int countDownLoadFile = this.testModuleService.getCountDownLoadFile(keyForSearch);
        mv.addObject("testFileCount", Integer.valueOf(countDownLoadFile));
        if(!StringUtils.isNullOrEmpty(keyForSearch)) {
            mv.addObject("pageNum", "1");
        } else {
            mv.addObject("pageNum", pageNum);
        }

        mv.addObject("pageSize", pageSize);
        mv.addObject("testFileList", list);
        mv.addObject("keySearch", keyForSearch);
        return mv;
    }

    @ResponseBody
    @RequestMapping({"/toDownLoadToLocal"})
    public Message toDownLoadToLocal(HttpServletResponse response, String fileId, String srcIpAddr, String fileName) {
        Object message = null;
        String conf_filename = Thread.currentThread().getContextClassLoader().getResource("fdfs_client.conf").getPath();

        try {
            ClientGlobal.init(conf_filename);
            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);
            TrackerClient e = new TrackerClient();
            TrackerServer trackerServer = e.getConnection();
            Object storageServer = null;
            StorageClient1 client = new StorageClient1(trackerServer, (StorageServer)storageServer);
            byte[] bytes = client.download_file1(fileId);
            response.setHeader("content-disposition", "attachment;filename=" + fileName);
            if(bytes != null) {
                ServletOutputStream os = response.getOutputStream();
                os.write(bytes);
                os.close();
                Fdfs_file f = this.testModuleService.getFileByFileId(fileId);
                if(f != null) {
                    this.testModuleService.saveFastFile(f);
                }
            }
        } catch (IOException var14) {
            logger.error("", var14);
        } catch (MyException var15) {
            logger.error("", var15);
        }

        return (Message)message;
    }

    @RequestMapping({"/accessFile"})
    public ModelAndView accessFile() throws IOException, MyException, JSchException {
        ModelAndView mv = new ModelAndView("testModule/accessFileCharts.jsp");
        List groups = this.monitorService.listGroupInfo();
        mv.addObject("groups", groups);
        return mv;
    }

    @ResponseBody
    @RequestMapping({"/tenFileDownLoad"})
    public Map<String, Object[]> tenFileDownLoad(String ip) {
        new HashMap();
        Map map = this.testModuleService.getAllFileListByTen(ip);
        return map;
    }

    @ResponseBody
    @RequestMapping({"/allFilePie"})
    public List<Line> allFilePie(String ip) {
        Line line = this.testModuleService.getAllFileListForPie(ip);
        ArrayList fileList = new ArrayList();
        fileList.add(line);
        return fileList;
    }

    @RequestMapping({"/downloadByApi"})
    public void downloadByApi(String fieldId, String fileName, HttpServletResponse response) throws IOException, MyException {
        ClientGlobal.init(Tools.getClassPath() + "fdfs_client.conf");
        logger.info("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
        logger.info("charset=" + ClientGlobal.g_charset);
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        if(trackerServer != null) {
            StorageClient1 client = new StorageClient1(trackerServer, (StorageServer)null);
            byte[] bytes = client.download_file1(fieldId);
            logger.info("length:" + bytes.length);
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            ServletOutputStream os = response.getOutputStream();
            os.write(bytes);
            os.close();
        }
    }
}
