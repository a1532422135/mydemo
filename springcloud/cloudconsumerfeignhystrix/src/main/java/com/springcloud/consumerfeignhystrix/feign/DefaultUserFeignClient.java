package com.springcloud.consumerfeignhystrix.feign;

import com.springcloud.config.Configuration;
import com.springcloud.consumerfeignhystrix.bean.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cloud-provider", fallback = HystrixClientFallback.class)
public interface DefaultUserFeignClient {

    @GetMapping(value = "/user/getUser")
    public User getUser(@RequestParam("id") long id);
}
