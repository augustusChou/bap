package com.github.bap.event.handler.controller;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 周广
 **/
@Data
public class ResultData<T> implements Serializable {

    public static final int SUCCESS_CODE = 0;
    public static final int FAIL_CODE = -1;

    public static final String SUCCESS_MSG = "操作成功";
    public static final String FAIL_MSG = "操作失败";

    private int code;
    private String msg;
    private T data;


    private ResultData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResultData<T> generate(int code, String msg, T data) {
        return new ResultData<>(code, msg, data);
    }

    public static <T> ResultData<T> success(T data) {
        return new ResultData<>(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static ResultData success() {
        return new ResultData<>(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public static ResultData fail() {
        return new ResultData<>(FAIL_CODE, FAIL_MSG, null);
    }

    public static ResultData fail(int code, String msg) {
        return new ResultData<>(code, msg, null);
    }
}
