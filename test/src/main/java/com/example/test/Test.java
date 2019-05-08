package com.example.test;

import com.example.test.design.observe.define.WeatherData;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Test {
    public static void main(String[] args) {
        Executor executor = Executors.newFixedThreadPool(10);
        while (true) {
            executor.execute(new Thread());
        }
    }

    static class Thread implements Runnable {

        @Override
        public void run() {
            System.out.println(1111);
            try {
                java.lang.Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
