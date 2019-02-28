package com.springcloud.provider.controller;

import com.springcloud.provider.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @RequestMapping("getUser")
    public User getUser(long id) {
        User user = new User();
        user.setAge(18);
        user.setId(id);
        user.setName("张三1");
        return user;
    }

}
