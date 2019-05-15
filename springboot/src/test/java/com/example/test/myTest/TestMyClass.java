package com.example.test.myTest;

import com.example.springboot.SpringbootApplication;
import com.example.springboot.controller.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class)
public class TestMyClass {
    @Autowired
    private UserController userController;
    @Test
    public void test(){
        System.out.println(userController.findUserByName("aaa"));
    }
}
