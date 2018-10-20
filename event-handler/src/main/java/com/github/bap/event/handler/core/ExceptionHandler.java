package com.github.bap.event.handler.core;

import com.github.bap.common.exceptions.ServerException;
import com.github.bap.event.handler.controller.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 周广
 **/
@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultData handler(Exception e) {
        if (e instanceof ServerException) {
            return ResultData.fail(ResultData.FAIL_CODE, e.getMessage());
        } else {
            log.error("捕获异常", e);
            return ResultData.fail();
        }
    }
}

