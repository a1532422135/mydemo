package com.example.springboot.dao;

import com.example.springboot.bean.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {
    List<User> findUsersByName(String name);
    @Cacheable(value = "user",key = "'allUser'")
    List<User> findAll();
}
