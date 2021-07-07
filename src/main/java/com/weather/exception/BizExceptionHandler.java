/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.weather.exception;

import com.weather.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler
 *
 * @author shenyuming
 */
@RestControllerAdvice
@Slf4j
public class BizExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BizException.class)
    public JsonResult<Object> handleBizException(BizException e) {
        return JsonResult.error(e.getMsg(), null, e.getCode());
    }

    /**
     * Exception
     */
    @ExceptionHandler(Exception.class)
    public JsonResult<Object> handleException(Exception e) {
        return JsonResult.error("unkown error", e.getMessage(), "4001");
    }
}
