//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adelmo.raid.domain.fdfs.action;

import com.adelmo.raid.domain.fdfs.service.UserService;
import com.adelmo.raid.domain.fdfs.vo.Message;
import com.adelmo.raid.domain.fdfs.vo.User;
import com.mysql.jdbc.StringUtils;
import java.io.IOException;
import java.util.List;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/user"})
public class UserAction {
    @Autowired
    private UserService userService;

    public UserAction() {
    }

    @RequestMapping({"/userlist"})
    public ModelAndView userlist(String username) throws IOException, MyException {
        ModelAndView mv = new ModelAndView("user/userlist.jsp");
        List userlist = this.userService.userlist(username);
        mv.addObject("userlist", userlist);
        return mv;
    }

    @RequestMapping({"/useradd"})
    public ModelAndView useradd(String id) throws IOException, MyException {
        ModelAndView mv = new ModelAndView("user/useradd.jsp");
        if(!StringUtils.isNullOrEmpty(id)) {
            User user = this.userService.findById(id);
            mv.addObject("id", user.getId());
            mv.addObject("name", user.getName());
            mv.addObject("psword", user.getPsword());
            mv.addObject("power", user.getPower());
        }

        return mv;
    }

    @ResponseBody
    @RequestMapping({"/saveuser"})
    public Message saveWarning(String name, String psword, String power) throws IOException, MyException {
        Message message = null;
        User userbyname = this.userService.findByName(name);
        String result;
        if(userbyname == null) {
            result = "添加成功";
            User user = new User();
            user.setName(name);
            user.setPsword(psword);
            user.setPower(power);
            this.userService.updateOrSaveUser(user);
            message = new Message();
            message.setStatusCode("200");
            message.setMessage(result);
        } else {
            result = "用户名重复";
            message = new Message();
            message.setStatusCode("300");
            message.setMessage(result);
        }

        return message;
    }

    @ResponseBody
    @RequestMapping({"/deluser"})
    public Message deluser(String ids) throws IOException, MyException {
        Message message = null;
        String[] id = ids.split(",");
        String[] arr$ = id;
        int len$ = id.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String i = arr$[i$];
            this.userService.delUser(i);
        }

        message = new Message();
        message.setStatusCode("200");
        message.setMessage("删除成功");
        return message;
    }
}
