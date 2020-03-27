package com.ywwl.customcard.handler;

import com.ywwl.customcard.model.RetResult;
import com.ywwl.customcard.util.RetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 用于处理通用异常
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RetResult<Object> bindException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMesssage = new StringBuilder("请求报文异常 ");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage.append(fieldError.getDefaultMessage()).append(", ");
        }
        errorMesssage.delete(errorMesssage.length()-2,errorMesssage.length()-1);
        return RetResponse.makeRsp(HttpStatus.BAD_REQUEST.value(), errorMesssage.toString());
    }
}