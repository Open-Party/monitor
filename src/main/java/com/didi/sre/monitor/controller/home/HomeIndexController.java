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
        return "home/login";
    }

    @RequestMapping(value = {"/doLogin"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getIndexToLogin(Model model, HttpServletRequest request,
                                      @RequestParam(value = "username", required = true)  String username,
                                      @RequestParam(value = "password", required = true)  String password) {
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

    @RequestMapping(value = {"/register", "/register.html"}, method = RequestMethod.GET)
    public String getIndexRegister(Model model, HttpServletRequest request) {
        return "home/register";
    }
}
