package com.example.springboot.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class User implements Serializable{
    @Id
    private Long id;

    private String userName;

    private String password;

    private String name;
}
