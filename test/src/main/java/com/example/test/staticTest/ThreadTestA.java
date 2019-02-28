package com.example.test.staticTest;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadTestA implements Runnable {

    private AtomicInteger a = null;

    @Override
    public void run() {
        try {
            a.incrementAndGet();
            if (a.get() == 2000000)
                System.out.println(Thread.currentThread().getName() + "  " + a);
        }catch (Exception e){
            log.error("异常为{},{}","aaa","aaa");
        }

    }
}
