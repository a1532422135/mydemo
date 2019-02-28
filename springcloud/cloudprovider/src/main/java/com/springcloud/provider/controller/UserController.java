package com.springcloud.provider.controller;

import com.springcloud.provider.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {
    @Value("${name}")
    private String providerName;

    @RequestMapping("getUser")
    public User getUser(long id) {
        User user = new User();
        user.setAge(18);
        user.setId(id);
        user.setName("张三");
        log.error("使用的provider是: {}", providerName);
        return user;
    }

    @RequestMapping("getName")
    public String getName() {
        return "张三";
    }
}
