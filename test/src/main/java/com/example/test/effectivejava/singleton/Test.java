package com.example.test.effectivejava.singleton;

import com.example.test.effectivejava.singleton.Elvis;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Administrator
 */
public class Test {
    private static int max = 100;

//    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//        Elvis elvis = Elvis.INSTANCE;
//        Class clazz = Class.forName("com.example.test.effectivejava.singleton.Elvis");
//        Constructor constructor = clazz.getDeclaredConstructor(String.class);
//        constructor.setAccessible(true);
//        System.out.println(elvis + " " + (Elvis) constructor.newInstance("haha"));
//    }

    public static void main(String[] args) {
        for(int i = 0;i<max;i++){
            new Runnable(){
                @Override
                public void run(){
                    Elvid elvid = Elvid.getInstance();
                    System.out.println(elvid);
                }
            }.run();
        }
    }
}
