package com.example.springboot.controller;

import com.example.springboot.Exception.MyException;
import com.example.springboot.bean.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserController{

    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

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
    @RequestMapping("/demo3")
    public String demo3(){
        return "demo3";//地址指向demo3.html
    }

}
