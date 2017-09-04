package com.didi.sre.monitor.controller.home;

import com.didi.sre.monitor.model.common.JsonResult;
import com.didi.sre.monitor.model.user.SysUserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.didi.sre.monitor.inject.WebSecurityConfig.SESSION_KEY;

/**
 * Created by soarpenguin on 17-8-12.
 */
@Controller
@Api(value = "Home")
public class HomeIndexController {
    private static Logger logger = Logger.getLogger(HomeIndexController.class);

    @ApiOperation(value="Get index page.",notes="requires noting")
    @RequestMapping(value = {"/", "/index", "/index.html"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String getIndexHome(Model model, HttpServletRequest request) {
        return "home/index";
    }

    @RequestMapping(value = {"/login", "/login.html"}, method = RequestMethod.GET)
    public String getIndexLogin(Model model, HttpServletRequest request) {
        if(request.getSession().getAttribute(SESSION_KEY) != null) {
            return "redirect:/";
        } else {
            return "home/login";
        }
    }

    @RequestMapping(value = {"/doLogin"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getIndexToLogin(Model model, HttpServletRequest request,
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

    @RequestMapping(value = {"/register", "/register.html"}, method = RequestMethod.GET)
    public String getIndexRegister(Model model, HttpServletRequest request) {
        return "home/register";
    }
}
