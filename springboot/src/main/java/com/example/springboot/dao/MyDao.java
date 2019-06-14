package com.example.springboot.dao;

import com.example.springboot.bean.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MyDao extends JpaRepository<User, Long> {

    @Query(value = "from User u where u.userName =?1")
    User getUser(String userName);

    @Query(value = "select * from user where user_name=?1", countQuery = "select count(*) from user where user_name=?1", nativeQuery = true)
    Page<User> findUser(String userName, Pageable pageable);
}
