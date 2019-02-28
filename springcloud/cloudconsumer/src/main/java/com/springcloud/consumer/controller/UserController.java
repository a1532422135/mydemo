package com.springcloud.consumer.controller;

import com.springcloud.consumer.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("getUser")
    public User getUser(long id) {
        User user = restTemplate.getForObject("http://cloud-provider/user/getUser?id=" + id, User.class);
        return user;
    }
}
