package com.ywwl.customcard.exception;

/**
 * 模版不支持异常类
 */
public class NotSupportTemplateException extends Exception {
    public NotSupportTemplateException () {
        super();
    }
    public NotSupportTemplateException(String message){
        super(message);
    }
}
