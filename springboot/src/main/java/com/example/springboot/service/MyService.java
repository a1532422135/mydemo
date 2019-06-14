package com.example.springboot.service;

import com.example.springboot.bean.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyService {
    User getUser(String userName);

    Page<User> findUser(String userName, Pageable pageable);
}
