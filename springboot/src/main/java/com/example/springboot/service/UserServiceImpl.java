package com.example.springboot.service;

import com.example.springboot.bean.User;
import com.example.springboot.dao.MyDao;
import com.example.springboot.dao.UserDao;
import com.example.springboot.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private MyDao myDao;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public List<User> findUserByName(String name) {
        Optional<User> optional = userDao.findById(1l);
        optional.orElseGet(User::new);
        return userDao.findUsersByName(name);
    }

    @Override
    public List<User> queryUserByName(String name) {
        return userMapper.queryUserByName(name);
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public int deleteByName(String name) {
        return userMapper.deleteUserByName(name);
    }

    @Transactional
    @Override
    public int updateUser(String name, Long id) {
        return userMapper.updateUser(name, id);
    }
}
