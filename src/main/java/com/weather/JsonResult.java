package com.weather;

import lombok.Data;

import java.io.Serializable;

@Data
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int status;
    private String msg;
    private String code;
    private T data;

    public JsonResult() {
    }

    public JsonResult(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static <T> JsonResult<T> ok(String message, T data) {
        JsonResult<T> jsonResult = new JsonResult<T>();
        jsonResult.setStatus(1);
        jsonResult.setCode("0");
        jsonResult.setMsg(message);
        jsonResult.setData(data);
        return jsonResult;
    }
    public static <T> JsonResult<T> okNoData(String message) {
        JsonResult<T> jsonResult = new JsonResult<T>();
        jsonResult.setStatus(1);
        jsonResult.setCode("0");
        jsonResult.setMsg(message);
        return jsonResult;
    }

    public static <T> JsonResult<T> error(String message, T data, String errCode) {
        JsonResult<T> jsonResult = new JsonResult<T>();
        jsonResult.setStatus(0);
        jsonResult.setMsg(message);
        jsonResult.setData(data);
        jsonResult.setCode(errCode);
        return jsonResult;
    }

}
