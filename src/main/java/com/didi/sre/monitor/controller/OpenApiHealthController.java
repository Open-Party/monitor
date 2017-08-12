package com.didi.sre.monitor.controller;

import com.didi.sre.monitor.model.common.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by soarpenguin on 17-8-12.
 */
@Controller
public class OpenApiHealthController {

    @RequestMapping(value = "/openapi/health", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JsonResult healthCheck () {
        return new JsonResult(true);
    }
}
