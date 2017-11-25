package com.didi.sre.monitor.controller.account;

import com.didi.sre.monitor.model.common.JsonResult;
import com.didi.sre.monitor.model.common.Md5PasswordEncoder;
import com.didi.sre.monitor.model.user.SysUserEntity;
import com.didi.sre.monitor.service.user.SysUserServiceImpl;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.didi.sre.monitor.inject.WebMvcConfig.SESSION_KEY;

/**
 * @author soarpenguin on 17-9-13.
 */
@Controller
public class AccountManageController {
    private static Logger logger = Logger.getLogger(AccountManageController.class);

    @Autowired
    SysUserServiceImpl sysUserServiceImpl;

    @Autowired
    DefaultKaptcha defaultKaptcha;

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

        String captchaId = (String) request.getSession().getAttribute("vrifyCode");
        String parameter = request.getParameter("vrifyCode");

        if (!captchaId.equals(parameter)) {
            jsonResult.setSuccess(false);
            jsonResult.setData("VrifyCode is wrong.");
        } else if(!"".equals(username) && password != "") {
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
        if (session.getAttribute(SESSION_KEY) != null) {
            session.removeAttribute(SESSION_KEY);
        }

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

        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        String encoderPassword = md5PasswordEncoder.encode(password);

        SysUserEntity user = new SysUserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoderPassword);
        logger.debug("User " + username + " do register.");

        return sysUserServiceImpl.insertSysUserEntity(user);
    }

    @RequestMapping(value = {"/account/userlist"}, method = RequestMethod.GET)
    public String getUserLists() {
        return "account/userlist";
    }

    @RequestMapping(value = {"/account/list"}, method = RequestMethod.GET)
    public String getLists() {
        return "account/list";
    }

    @RequestMapping(value = "/account/userjson", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getUserJson() {
        JsonResult jsonResult = new JsonResult(false);
        List<SysUserEntity> users = sysUserServiceImpl.getSysUserList();

        if (users != null) {
            jsonResult.setData(users);
            jsonResult.setSuccess(true);
        }

        return jsonResult;
    }


    @RequestMapping("/imgvrifyControllerDefaultKaptcha")
    public ModelAndView imgvrifyControllerDefaultKaptcha(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
        ModelAndView andView = new ModelAndView();
        String captchaId = (String) httpServletRequest.getSession().getAttribute("vrifyCode");
        String parameter = httpServletRequest.getParameter("vrifyCode");
        System.out.println("Session  vrifyCode "+captchaId+" form vrifyCode "+parameter);

        if (!captchaId.equals(parameter)) {
            andView.addObject("info", "错误的验证码");
            andView.setViewName("index");
        } else {
            andView.addObject("info", "登录成功");
            andView.setViewName("succeed");

        }
        return andView;
    }

    @RequestMapping("/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        byte[] captchaChallengeAsJpeg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            httpServletRequest.getSession().setAttribute("vrifyCode", createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
