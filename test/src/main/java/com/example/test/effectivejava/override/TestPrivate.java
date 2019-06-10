package com.example.test.effectivejava.override;

public class TestPrivate {
    public static void main(String[] args) {
        String a = ",,a,a,b,,c,c,dd,,,";
        String[] b = a.split(",",-1);
        for(String c:b){
            System.out.println(c);
        }
    }

    static class A {
        public void m1(String s) {
            System.out.println(s + "call a m1");
        }

        public void m2(String s) {
            System.out.println(s + "call a m2");
            m1(s);
        }
    }

    static class B extends A {
        @Override
        public void m1(String s) {
            System.out.println(s + "call b m1");
            m2(s);
        }
    }
}

