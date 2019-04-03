package com.example.spring;

import com.example.spring.beans.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class SpringStart {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Student student = (Student) classPathXmlApplicationContext.getBean("student");
        log.info(student.getObject().toString());
        log.info(student.getObject().toString());
        log.info(student.sout().toString());
    }
}
