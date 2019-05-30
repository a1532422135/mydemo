package com.example.test.effectivejava.override;

public class MyTest extends Test {
    @Override
    public int a() {
        return 2;
    }

    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test.a());
    }
}
