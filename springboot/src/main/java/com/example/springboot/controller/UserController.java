package com.example.springboot.controller;

import com.example.springboot.Exception.MyException;
import com.example.springboot.bean.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("queryAllUser")
    public List<User> queryAllUser() {
        return userService.findAll();
    }

    @RequestMapping("findUserByName")
    public List<User> findUserByName(String name) {
        return userService.findUserByName(name);
    }

    @RequestMapping("queryUserByName")
    public List<User> queryUserByName(String name) {
        List<User> list = userService.queryUserByName(name);
        return list;
    }

    @RequestMapping("insertUser")
    public List<User> insertUser(User user) {
        int a = userService.insertUser(user);
        return userService.findAll();
    }

    @RequestMapping("deleteByName")
    public List<User> deleteByName(String name) {
        int a = userService.deleteByName(name);
        return userService.findAll();
    }

    @RequestMapping("updateUser")
    public List<User> updateUser(String name, Long id) {
        int a = userService.updateUser(name, id);
        return userService.findAll();
    }
}
