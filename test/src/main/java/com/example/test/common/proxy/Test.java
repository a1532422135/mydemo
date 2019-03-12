package com.example.test.common.proxy;

public class Test {
    public static void main(String[] args) {
        LogHandler logHandler = new LogHandler();
        UserService userService = (UserService) logHandler.newProxyInstance(new UserServiceImpl());
        userService.addUser("1111","zhangsan");
    }
}
