package com.springcloud.consumerfeignhystrix.feign;

import com.springcloud.consumerfeignhystrix.bean.User;
import org.springframework.stereotype.Component;

@Component
public class HystrixClientFallback implements DefaultUserFeignClient {
    @Override
    public User getUser(long id) {
        User user = new User();
        user.setName("王五");
        return user;
    }
}
