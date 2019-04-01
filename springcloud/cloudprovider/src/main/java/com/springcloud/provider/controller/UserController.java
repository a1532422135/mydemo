package com.springcloud.provider.controller;

import com.springcloud.provider.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @RequestMapping(value = "getUser", produces = {"application/json;charset=UTF-8"})
    public User getUser(long id) {
        User user = new User();
        user.setAge(18);
        user.setId(id);
        user.setName("张三");
        return user;
    }

    @RequestMapping(value = "getUser1", produces = {"application/json;charset=UTF-8"})
    public User getUser1(@RequestBody User user) {
        user.setAge(18);
        user.setId(user.getId());
        user.setName("张三");
        return user;
    }

}
