package com.didi.sre.monitor.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by soarpenguin on 17-8-12.
 */
@Controller
public class HomeIndexController {
    @RequestMapping(value = {"/", "/index", "/index.html"}, method = RequestMethod.GET)
    public String getIndexHome(Model model, HttpServletRequest request) {
        return "home/index";
    }
}
