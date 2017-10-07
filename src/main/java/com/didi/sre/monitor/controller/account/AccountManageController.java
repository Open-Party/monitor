package com.didi.sre.monitor.controller.account;

import com.didi.sre.monitor.model.common.JsonResult;
import com.didi.sre.monitor.model.common.MD5PasswordEncoder;
import com.didi.sre.monitor.model.user.SysUserEntity;
import com.didi.sre.monitor.service.user.SysUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.didi.sre.monitor.inject.WebMvcConfig.SESSION_KEY;

/**
 * Created by soarpenguin on 17-9-13.
 */
@Controller
public class AccountManageController {
    private static Logger logger = Logger.getLogger(AccountManageController.class);

    @Autowired
    SysUserService sysUserService;

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String getLogin() {
        return "home/login";
    }

    @RequestMapping(value = {"/doLogin"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult doLogin(HttpServletRequest request,
                              @RequestParam(value = "username")  String username,
                              @RequestParam(value = "password")  String password) {
        JsonResult jsonResult = new JsonResult(false);

        if(!username.equals("") && password != "") {
            SysUserEntity user = new SysUserEntity();
            user.setId(1L);
            user.setUsername(username);
            user.setPassword(password);
            request.getSession().setAttribute("user", user);
            jsonResult.setSuccess(true);
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setData("Check user info failed.");
        }

        return jsonResult;
    }

    @RequestMapping(value = {"/signOut"}, method = RequestMethod.GET)
    public String doSignOut(HttpServletRequest request) {

        HttpSession session = request.getSession();
        if (session.getAttribute(SESSION_KEY) != null)
            session.removeAttribute(SESSION_KEY);

        return "redirect:/login";
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public String getRegister() {
        return "home/register";
    }

    @RequestMapping(value = "/doRegister", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult doRegister(@RequestParam(value = "username")  String username,
                                 @RequestParam(value = "email") String email,
                                 @RequestParam(value = "password")  String password) {

        MD5PasswordEncoder md5PasswordEncoder = new MD5PasswordEncoder();
        String encoderPassword = md5PasswordEncoder.encode(password);

        SysUserEntity user = new SysUserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoderPassword);
        logger.debug("User " + username + " do register.");

        return sysUserService.insertSysUserEntity(user);
    }

    @RequestMapping(value = {"/account/userlist"}, method = RequestMethod.GET)
    public String getUserLists() {
        return "account/userlist";
    }

    @RequestMapping(value = "/account/userjson", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getUserJson() {
        JsonResult jsonResult = new JsonResult(false);
        List<SysUserEntity> users = sysUserService.getSysUserList();

        if (users != null) {
            jsonResult.setData(users);
            jsonResult.setSuccess(true);
        }

        return jsonResult;
    }
}
