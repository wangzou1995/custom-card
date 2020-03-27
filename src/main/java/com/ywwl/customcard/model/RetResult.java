package com.ywwl.customcard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetResult<T> {

    public int code;

    private String msg;

    private T data;

    public RetResult (int code , String msg) {
        this.code = code;
        this.msg = msg;
    }

}