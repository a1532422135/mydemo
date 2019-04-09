package com.example.test.effectivejava.singleton;

import lombok.Getter;

/**
 * @author Administrator
 * 懒汉
 */
public class Elvis {
    @Getter
    private String info;
    private Elvis(String info){
        this.info = info;
    }
    public static final Elvis INSTANCE = new Elvis("info");
}
