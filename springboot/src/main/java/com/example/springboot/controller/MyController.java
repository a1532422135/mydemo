package com.example.springboot.controller;

import com.example.springboot.bean.User;
import com.example.springboot.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("my")
public class MyController {
    @Autowired
    private MyService myService;

    @RequestMapping(value = "myController", method = RequestMethod.GET)
    public User getUser(String userName) {
        User user = myService.getUser(userName);
        return user;
    }

    @RequestMapping(value = "pageController", method = RequestMethod.GET)
    public Page<User> findUser(String userName) {
        Sort sort = new Sort(Sort.Direction.DESC,"name");
        Page<User> page = myService.findUser(userName,PageRequest.of(1,3, sort));
        return page;
    }
}
