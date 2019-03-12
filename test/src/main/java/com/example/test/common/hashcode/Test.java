package com.example.test.common.hashcode;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class Test {
    String name;

    public Test(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Map<Test, String> map = new HashMap<Test, String>(4);
        map.put(new Test("hello"), "hello");
        String hello = map.get(new Test("hello"));
        log.error(hello);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Test test = (Test) object;
        return Objects.equals(name, test.name);
    }
    @Override
    public int hashCode(){
        return Objects.hash(name);
    }
}
