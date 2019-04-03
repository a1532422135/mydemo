package com.example.spring.beans;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class Student {
    private int age;
    private int code;

    public Object getObject() {
        Teacher teacher = getTeacher();
        return teacher;
    }

    public Object sout() {
        return this;
    }

    public abstract Teacher getTeacher();
}
