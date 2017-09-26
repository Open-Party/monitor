package com.didi.sre.monitor.controller.errors;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by soarpenguin on 17-8-14.
 */
@Controller
public class HttpErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH, method = RequestMethod.GET)
    public String handleError(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status.is4xxClientError()) {
            return "errors/404";
        } else if (status.is5xxServerError()) {
            return "errors/500";
        }
        return getErrorPath();
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}