package com.example.springboot.Exception;

import lombok.Data;

@Data
public class MyException extends RuntimeException {
    private String code;
    private String msg;

    public MyException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
