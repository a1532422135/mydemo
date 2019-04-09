package com.example.test.effectivejava.singleton;

/**
 * @author Administrator
 * 饿汉
 */
public class Elvid {
    private static Elvid instance;

    private Elvid() {
        System.out.println("haha");
    }

    public static Elvid getInstance() {
        if (instance == null) {
            synchronized (Elvid.class) {
                if (instance == null) {
                    instance = new Elvid();
                }
            }
        }
        return instance;
    }

}
