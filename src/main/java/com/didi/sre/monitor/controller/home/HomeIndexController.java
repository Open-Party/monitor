package com.didi.sre.monitor.controller.home;

import com.didi.sre.monitor.model.common.JsonResult;
import com.didi.sre.monitor.model.common.MD5PasswordEncoder;
import com.didi.sre.monitor.model.user.SysUserEntity;
import com.didi.sre.monitor.service.user.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.didi.sre.monitor.inject.WebMvcConfig.SESSION_KEY;

/**
 * Created by soarpenguin on 17-8-12.
 */
@Controller
@Api(value = "Home")
public class HomeIndexController {
    private static Logger logger = Logger.getLogger(HomeIndexController.class);

    @Autowired
    SysUserService sysUserService;

    @ApiOperation(value="Get index page.",notes="requires noting")
    @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String getIndexHome(Model model, HttpServletRequest request) {
        return "home/index";
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String getIndexLogin(Model model, HttpServletRequest request) {
        return "home/login";
    }

    @RequestMapping(value = {"/doLogin"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult doLogin(Model model, HttpServletRequest request,
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
    public String doSignOut(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        if (session.getAttribute(SESSION_KEY) != null)
            session.removeAttribute(SESSION_KEY);

        return "redirect:/login";
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public String getRegister(Model model, HttpServletRequest request) {
        return "home/register";
    }

    @RequestMapping(value = "/doRegister", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult doRegister(Model model, HttpServletRequest request,
                                 @RequestParam(value = "username")  String username,
                                 @RequestParam(value = "email") String email,
                                 @RequestParam(value = "password")  String password) {

        MD5PasswordEncoder md5PasswordEncoder = new MD5PasswordEncoder();
        String encoderPassword = md5PasswordEncoder.encode(password);

        SysUserEntity user = new SysUserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoderPassword);

        return sysUserService.insertSysUserEntity(user);
    }
}
