package com.example.springboot.dao;

import com.example.springboot.bean.User;
import org.springframework.data.repository.CrudRepository;

public interface MyDao extends CrudRepository<User,Integer> {
}
