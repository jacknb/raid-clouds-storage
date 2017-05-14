//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adelmo.raid.domain.fdfs.action;

import com.adelmo.raid.domain.fdfs.service.UserService;
import com.adelmo.raid.domain.fdfs.vo.Message;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/main"})
public class MainAction {
    @Autowired
    private UserService userService;

    public MainAction() {
    }

    @RequestMapping({"/index"})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("main/index.jsp");
        return mv;
    }

    @RequestMapping({"/login"})
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "main/login.jsp";
    }

    @ResponseBody
    @RequestMapping({"/loginDo"})
    public Message loginDo(String username, String password, HttpServletRequest request, HttpServletResponse response) throws IOException, MyException {
        boolean res = this.userService.login(username, password);
        if(res) {
            HttpSession message2 = request.getSession();
            message2.setAttribute("username", username);
            message2.setAttribute("userpower", this.userService.findByName(username).getPower());
            Message message1 = new Message("200", "登录成功", "", "", "", "");
            return message1;
        } else {
            Message message = new Message("300", "用户名或密码错误", "", "", "", "");
            return message;
        }
    }

    @RequestMapping({"/loginout"})
    public void loginout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("login.shtml");
    }
}
