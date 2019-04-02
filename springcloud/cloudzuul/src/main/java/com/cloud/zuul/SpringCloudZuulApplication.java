package com.cloud.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableZuulProxy
public class SpringCloudZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudZuulApplication.class);
    }
}
