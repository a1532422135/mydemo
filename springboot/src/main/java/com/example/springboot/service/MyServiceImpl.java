package com.example.springboot.service;

import com.example.springboot.bean.User;
import com.example.springboot.dao.MyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MyServiceImpl implements MyService {

    @Autowired
    private MyDao myDao;

    @Override
    public User getUser(String userName) {
        return myDao.getUser(userName);
    }

    @Override
    public Page<User> findUser(String userName, Pageable pageable) {
        return myDao.findUser(userName,pageable);
    }
}
