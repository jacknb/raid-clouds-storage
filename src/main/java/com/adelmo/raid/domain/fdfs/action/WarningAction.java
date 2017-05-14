//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adelmo.raid.domain.fdfs.action;

import com.adelmo.raid.domain.fdfs.service.MonitorService;
import com.adelmo.raid.domain.fdfs.service.WarningService;
import com.adelmo.raid.domain.fdfs.vo.Group;
import com.adelmo.raid.domain.fdfs.vo.Message;
import com.adelmo.raid.domain.fdfs.vo.PageInfo;
import com.adelmo.raid.domain.fdfs.vo.Storage;
import com.adelmo.raid.domain.fdfs.vo.WarningData;
import com.adelmo.raid.domain.fdfs.vo.WarningUser;
import com.jcraft.jsch.JSchException;
import com.mysql.jdbc.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/warning"})
public class WarningAction {
    @Autowired
    private WarningService warningService;
    @Autowired
    private MonitorService monitorService;

    public WarningAction() {
    }

    @RequestMapping({"/warningValue"})
    public ModelAndView warningValue(String wdIpAddr, PageInfo pageInfo) throws IOException, MyException {
        ModelAndView mv = new ModelAndView("warning/warningValue.jsp");
        WarningData wd = new WarningData();
        wd.setWdIpAddr(wdIpAddr);
        List warningDataLists = this.warningService.findWarning(wd, pageInfo);
        Iterator i$ = warningDataLists.iterator();

        while(i$.hasNext()) {
            WarningData warningDataList = (WarningData)i$.next();
            List storage = this.monitorService.listStorageTop(warningDataList.getWdIpAddr());
            if(!storage.isEmpty()) {
                warningDataList.setWdGroupName(((Storage)storage.get(0)).getGroupName());
            }
        }

        mv.addObject("warningValues", warningDataLists);
        mv.addObject("wdIpAddr", wdIpAddr);
        mv.addObject("pageInfoList", pageInfo);
        return mv;
    }

    @RequestMapping({"/warningEdit"})
    public ModelAndView warningEdit(String id) throws IOException, MyException {
        ModelAndView mv = new ModelAndView("warning/warningEdit.jsp");
        if(!StringUtils.isNullOrEmpty(id)) {
            WarningData wd = this.warningService.findById(id);
            mv.addObject("id", wd.getId());
            mv.addObject("wdIpAddr", wd.getWdIpAddr());
            mv.addObject("wdFreeMB", Long.valueOf(wd.getWdFreeMB()));
            mv.addObject("wdCpu", wd.getWdCpu());
            mv.addObject("wdMem", Float.valueOf(wd.getWdMem()));
        }

        return mv;
    }

    @ResponseBody
    @RequestMapping({"/saveWarning"})
    public Message saveWarning(String warningdataid, String ips, String wdFreeMB, String wdCpu, String wdMem) throws IOException, MyException, JSchException {
        Message message = null;
        String result = "操作成功";
        message = new Message();
        List groupList = this.monitorService.listGroupInfo();
        Iterator wd = groupList.iterator();

        while(wd.hasNext()) {
            Group group = (Group)wd.next();
            Iterator i$ = group.getStorageList().iterator();

            while(i$.hasNext()) {
                Storage storage = (Storage)i$.next();
                if(storage.getIpAddr().equals(ips)) {
                    if(!storage.getCurStatus().equals("ACTIVE")) {
                        message.setStatusCode("300");
                        message.setMessage("服务器已停止工作");
                        return message;
                    }

                    if(storage.getFreeMB() < Long.decode(wdFreeMB).longValue() * 1024L) {
                        message.setStatusCode("300");
                        message.setMessage("容量预警值大于当前可用容量");
                        return message;
                    }
                }
            }
        }

        WarningData wd1 = new WarningData();
        if(!StringUtils.isNullOrEmpty(warningdataid)) {
            wd1.setId(warningdataid);
        }

        wd1.setWdIpAddr(ips);
        wd1.setWdCpu(wdCpu);
        wd1.setWdFreeMB(Long.decode(wdFreeMB).longValue());
        wd1.setWdMem(Float.parseFloat(wdMem));
        this.warningService.updateWarning(wd1);
        message.setStatusCode("200");
        message.setMessage(result);
        return message;
    }

    @ResponseBody
    @RequestMapping({"/delWarning"})
    public Message delWarning(String ids) throws IOException, MyException {
        Message message = null;
        String[] id = ids.split(",");
        String[] arr$ = id;
        int len$ = id.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String i = arr$[i$];
            this.warningService.delWarning(i);
        }

        message = new Message();
        message.setStatusCode("200");
        message.setMessage("操作成功");
        return message;
    }

    @ResponseBody
    @RequestMapping({"/selectIp"})
    public List<Message> selectIp() throws IOException, MyException, JSchException {
        ArrayList ips = new ArrayList();
        List groupList = this.monitorService.listGroupInfo();
        Iterator i$ = groupList.iterator();

        while(i$.hasNext()) {
            Group group = (Group)i$.next();
            List storageList = group.getStorageList();
            Iterator i$1 = storageList.iterator();

            while(i$1.hasNext()) {
                Storage storage = (Storage)i$1.next();
                if(this.warningService.findByIp(storage.getIpAddr()).isEmpty()) {
                    ips.add(new Message(storage.getIpAddr()));
                }
            }
        }

        return ips;
    }

    @RequestMapping({"/warUserList"})
    public ModelAndView warUserList(String wusername, PageInfo pageInfo) throws IOException, MyException {
        ModelAndView mv = new ModelAndView("warning/warUserList.jsp");
        WarningUser wu = new WarningUser();
        wu.setName(wusername);
        List warningUserList = this.warningService.findWarUser(wu, pageInfo);
        mv.addObject("warUserLists", warningUserList);
        mv.addObject("wusername", wusername);
        mv.addObject("pageInfoList", pageInfo);
        return mv;
    }

    @RequestMapping({"/warUserAdd"})
    public ModelAndView warUserAdd(String id) throws IOException, MyException {
        ModelAndView mv = new ModelAndView("warning/warUserAdd.jsp");
        if(!StringUtils.isNullOrEmpty(id)) {
            WarningUser wu = this.warningService.findUserId(id);
            mv.addObject("id", wu.getId());
            mv.addObject("name", wu.getName());
            mv.addObject("phone", wu.getPhone());
            mv.addObject("email", wu.getEmail());
        }

        return mv;
    }

    @ResponseBody
    @RequestMapping({"/delWarUser"})
    public Message delWarUser(String ids) throws IOException, MyException {
        Message message = null;
        String[] id = ids.split(",");
        String[] arr$ = id;
        int len$ = id.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String i = arr$[i$];
            this.warningService.delWarUser(i);
        }

        message = new Message();
        message.setStatusCode("200");
        message.setMessage("操作成功");
        return message;
    }

    @ResponseBody
    @RequestMapping({"/saveWarUser"})
    public Message saveWarUser(String wuid, String wuname, String wuphone, String wuemail) throws IOException, MyException {
        Message message = null;
        String result = "操作成功";
        WarningUser wu = new WarningUser();
        if(wuphone.length() > 11) {
            result = "操作失败";
            message = new Message();
            message.setStatusCode("304");
            message.setMessage("电话号较长");
        } else {
            if(!StringUtils.isNullOrEmpty(wuid)) {
                wu.setId(wuid);
            }

            wu.setName(wuname);
            wu.setPhone(wuphone);
            wu.setEmail(wuemail);
            this.warningService.updateWarUser(wu);
            message = new Message();
            message.setStatusCode("200");
            message.setMessage("操作成功");
        }

        return message;
    }
}
