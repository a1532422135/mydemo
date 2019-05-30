package com.example.test.effectivejava.override;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("ad");
        list.add("adc");
        list = list.stream().filter(a -> a.length() < 1).collect(Collectors.toList());
        System.out.println(list);
    }

    public int a() {
        return 1;
    }
}
