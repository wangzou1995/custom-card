package com.ywwl.customcard.util;

import com.ywwl.customcard.model.RetCode;
import com.ywwl.customcard.model.RetResult;



public class RetResponse {

    private final static String SUCCESS = "success";

    public static <T> RetResult<T> makeOKRsp() {
        return new RetResult<>(RetCode.SUCCESS.code, SUCCESS);
    }

    public static <T> RetResult<T> makeOKRsp(T data) {
        return new RetResult<>(RetCode.SUCCESS.code, SUCCESS, data);
    }

    public static <T> RetResult<T> makeErrRsp(String message) {
        return new RetResult<>(RetCode.FAIL.code, SUCCESS);
    }

    public static <T> RetResult<T> makeRsp(int code, String msg) {
        return new RetResult<>(code, msg);
    }

    public static <T> RetResult<T> makeRsp(int code, String msg, T data) {
        return new RetResult<>(code, msg, data);
    }
}