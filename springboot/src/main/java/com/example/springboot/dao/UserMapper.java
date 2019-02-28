package com.example.springboot.dao;

import com.example.springboot.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface UserMapper {
    @Cacheable(value = "user", key = "#p0")
    List<User> queryUserByName(@Param(value = "name") String name);

    @CacheEvict(value = "user",key = "#p0.name")
    int insertUser(@Param(value = "user") User user);

    @CacheEvict(value = "user", key = "#p0")
    int deleteUserByName(@Param(value = "name") String name);

    int updateUser(@Param(value = "name") String name, @Param(value = "id") Long id);
}
