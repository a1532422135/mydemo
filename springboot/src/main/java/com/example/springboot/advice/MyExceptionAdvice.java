package com.example.springboot.advice;

import com.example.springboot.Exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class MyExceptionAdvice {
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception e, HttpServletRequest request) {
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", e);
        log.error("出现异常 {}", e);
        return map;
    }

    @ExceptionHandler(value = MyException.class)
    public Map myErrorHandler(MyException e) {
        Map map = new HashMap();
        map.put("code", e.getCode());
        map.put("msg", e.getMsg());
        return map;
    }
}
