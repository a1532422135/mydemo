package com.example.springboot.controller;

import com.example.springboot.bean.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "queryAllUserDao", method = RequestMethod.GET)
    public List<User> queryAllUser() {
        return userService.findAll();
    }

    @RequestMapping(value = "findUserByNameDao", method = RequestMethod.GET)
    public List<User> findUserByName(String name) {
        return userService.findUserByName(name);
    }

    @RequestMapping(value = "queryUserByNameMapper", method = RequestMethod.GET)
    @ResponseBody
    public List<User> queryUserByName(String name) {
        List<User> list = userService.queryUserByName(name);
        return list;
    }

    @RequestMapping(value = "insertUserMapper", method = RequestMethod.POST)
    public List<User> insertUser(User user) {
        int a = userService.insertUser(user);
        return userService.findAll();
    }

    @RequestMapping(value = "deleteByNameMapper", method = RequestMethod.GET)
    public List<User> deleteByName(String name) {
        int a = userService.deleteByName(name);
        return userService.findAll();
    }

    @RequestMapping(value = "updateUserMapper", method = RequestMethod.POST)
    public List<User> updateUser(String name, Long id) {
        int a = userService.updateUser(name, id);
        return userService.findAll();
    }

    @RequestMapping(value = "/getRedis", method = RequestMethod.GET)
    public Object demo3() {
        return redisTemplate.opsForValue().get("1");//地址指向demo3.html
    }

    @RequestMapping(value = "/setRedis", method = RequestMethod.POST)
    public String redis(User user) {
        redisTemplate.opsForValue().set("1", user);
        return "success";
    }

}
