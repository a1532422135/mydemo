package com.springcloud.consumerfeign.controller;

import com.springcloud.consumerfeign.bean.User;
import com.springcloud.consumerfeign.feign.DefaultUserFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Api(value = "user", tags = "用户")
@Slf4j
public class UserController {

    @Autowired
    private DefaultUserFeignClient userFeignClient;

    @RequestMapping(value = "getUser", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据id查找用户", notes = "根据id查找用户")
    public User getUser(long id) {
        User user = userFeignClient.getUser(id);
        return user;
    }

//    @RequestMapping(value = "getUser1", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
//    @ApiOperation(value = "根据id查找用户", notes = "根据id查找用户")
//    public User getUser1(long id) {
//        User user = new User();
//        user.setId(id);
//        user = userFeignClient.getUser1(user);
//        return user;
//    }
}
