package com.didi.sre.monitor.model.common;

import java.io.Serializable;

/**
 * Created by soarpenguin on 17-8-12.
 */

public class JsonResult implements Serializable {

    private static final long serialVersionUID = 3047368827628740529L;

    /**
     * 是否执行成功
     */
    private boolean success;
    /**
     * 返回值内容
     */
    private Object data;
    /**
     * 错误信息
     */
    private String message;

    /**
     * 错误码
     */
    private String code;

    /**
     * http请求返回码
     */
    private transient Integer httpCode;

    /**
     * 执行请求机器ip，以便排查问题
     */
    private transient String ip;

    public JsonResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public JsonResult(boolean success, Object o) {
        this.success = success;
        if (success) {
            this.data = o;
        } else {
            this.message = (String) o;
        }
    }
    public JsonResult(boolean success) {
        this(success, null);
    }

    public JsonResult(){}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "success=" + success +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
