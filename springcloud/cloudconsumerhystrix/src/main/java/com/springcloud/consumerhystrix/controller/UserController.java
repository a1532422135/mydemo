package com.springcloud.consumerhystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springcloud.consumerhystrix.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("user")
@Api(value = "user", tags = "用户")
@Slf4j
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "getUser", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据id查找用户", notes = "根据id查找用户")
    @HystrixCommand(fallbackMethod = "getUserFallback")
    public User getUser(long id) {
        User user = restTemplate.getForObject("http://cloud-provider/user/getUser?id=" + id, User.class);
        return user;
    }

    public User getUserFallback(long id) {
        User user = new User();
        user.setName("王五");
        return user;
    }
}
