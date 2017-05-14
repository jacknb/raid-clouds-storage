package com.adelmo.raid.domain.fdfs.action;

import com.adelmo.raid.domain.fdfs.service.MonitorService;
import com.adelmo.raid.domain.fdfs.vo.Group;
import com.adelmo.raid.domain.fdfs.vo.Line;
import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletContext;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/monitor"})
public class MonitorAction implements ServletContextAware {
    private ServletContext servletContext;
    @Autowired
    private MonitorService monitorService;

    public MonitorAction() {
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping({"/capacity"})
    public ModelAndView capacity() throws IOException, MyException, JSchException {
        ModelAndView mv = new ModelAndView("monitor/capacity.jsp");
        mv.addObject("groupInfo", this.monitorService.listGroupInfo());
        return mv;
    }

    @ResponseBody
    @RequestMapping({"/listGroupInfo"})
    public List<Group> listStorageInfo() throws IOException, MyException, JSchException {
        return this.monitorService.listGroupInfo();
    }

    @RequestMapping({"/netTraffic"})
    public ModelAndView netTraffic() throws IOException, MyException, JSchException {
        ModelAndView mv = new ModelAndView("monitor/netTraffic.jsp");
        mv.addObject("groupInfo", this.monitorService.listGroupInfo());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        mv.addObject("end", sdf.format(calendar.getTime()));
        calendar.add(10, -1);
        mv.addObject("start", sdf.format(calendar.getTime()));
        return mv;
    }

    @RequestMapping({"/performance"})
    public ModelAndView performance() throws IOException, MyException, JSchException {
        ModelAndView mv = new ModelAndView("monitor/performance.jsp");
        List groups = this.monitorService.listGroupInfo();
        mv.addObject("groups", groups);
        return mv;
    }

    @ResponseBody
    @RequestMapping({"/getPerformanceLine"})
    public List<Line> getPerformanceLine(String groupName) throws IOException, MyException {
        return this.monitorService.listStorageLines(groupName);
    }

    @ResponseBody
    @RequestMapping({"/getNetTrafficLine"})
    public List<Line> getNetTrafficLine(String ip, String start, String end) throws IOException, MyException {
        return this.monitorService.getNetTrafficLines(ip, start, end);
    }

    @ResponseBody
    @RequestMapping({"/capactityStorage"})
    public List<Line> capactityStorage(String ip, String startTime, String endTime) throws IOException, MyException {
        System.out.println(ip);
        ArrayList result = new ArrayList();
        result.add(this.monitorService.getListStoragesInfo(ip, startTime, endTime));
        return result;
    }

    @RequestMapping({"/storageInfo"})
    public ModelAndView storageInfo(String ip) throws IOException, MyException {
        ModelAndView mv = new ModelAndView("monitor/storageInfo.jsp");
        mv.addObject("storage", this.monitorService.getStorageByIp(ip));
        return mv;
    }

    @ResponseBody
    @RequestMapping({"/fileCountStorage"})
    public List<Line> fileCountStorage(String ip, String startTime, String endTime) throws IOException, MyException {
        new ArrayList();
        List result = this.monitorService.getListFileCountStorage(ip, startTime, endTime);
        return result;
    }

    @RequestMapping({"/testUpload"})
    public ModelAndView testUpload() throws IOException, MyException {
        ModelAndView mv = new ModelAndView("monitor/testUpload.jsp");
        return mv;
    }

    @RequestMapping(
        value = {"/oneFileUpload"},
        method = {RequestMethod.POST}
    )
    public ModelAndView handleFormUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) throws IOException, MyException {
        ModelAndView mv = new ModelAndView("main/index.jsp");
        System.out.println("name：" + name);
        System.out.println("上传文件：" + file.getOriginalFilename());
        Object f = null;
        return mv;
    }
}
