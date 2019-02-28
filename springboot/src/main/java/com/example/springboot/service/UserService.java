package com.example.springboot.service;

import com.example.springboot.bean.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    List<User> findUserByName(String name);

    List<User> queryUserByName(String name);

    int insertUser(User user);

    int deleteByName(String name);

    int updateUser(String name, Long id);
}
