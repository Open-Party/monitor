package com.didi.sre.monitor.controller;

import com.didi.sre.monitor.model.common.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author soarpenguin on 17-8-12.
 */
@RestController
public class OpenApiHealthController {

    @RequestMapping(value = "/openapi/health", method = RequestMethod.GET, produces = "application/json")
    public JsonResult healthCheck () {
        return new JsonResult(true);
    }
}
