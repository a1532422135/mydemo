package com.springcloud.consumer.controller;

import com.springcloud.consumer.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("getUser")
    public User getUser(long id) {
        return restTemplate.getForObject("http://localhost:8078/user/getUser?id=" + id, User.class);
    }
}
