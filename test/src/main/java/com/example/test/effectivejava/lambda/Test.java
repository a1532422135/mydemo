package com.example.test.effectivejava.lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> lists = new ArrayList<String>();
        lists.add("1");
        lists.add("322");
        lists.add("33");
        System.out.println(lists);
        Collections.sort(lists, Comparator.comparingInt(String::length));
        System.out.println(lists);
    }
}
