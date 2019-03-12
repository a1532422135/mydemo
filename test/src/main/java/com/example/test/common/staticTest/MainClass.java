package com.example.test.common.staticTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainClass {
    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        ThreadTestA threadTestA = new ThreadTestA();
        ThreadTestB threadTestB = new ThreadTestB();
        for (int i = 0; i < 2000000; i++) {
            executorService.submit(threadTestA);
        }
        executorService.shutdown();
        System.out.println("程序共耗时:" + (System.currentTimeMillis() - begin));
    }
}
