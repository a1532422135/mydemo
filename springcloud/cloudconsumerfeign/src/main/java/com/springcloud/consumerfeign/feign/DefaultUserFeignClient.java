package com.springcloud.consumerfeign.feign;

import com.springcloud.consumerfeign.bean.User;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloud-provider")
public interface DefaultUserFeignClient {

    @GetMapping(value = "/user/getUser")
    public User getUser(@RequestParam("id") long id);
//
//    @PostMapping(value = "/user/getUser1")
//    public User getUser1(@RequestBody User user);

//    @RequestLine("GET /user/getUser/id")
//    public User getUser(@Param("id") long id);
}
