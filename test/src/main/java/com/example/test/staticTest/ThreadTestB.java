package com.example.test.staticTest;

public class ThreadTestB implements Runnable {
    private  int a = 1;

    @Override
    public void run() {
        synchronized (Integer.valueOf(a)){
            a = a + 1;
        }
        if (a == 2000000)
            System.out.println(Thread.currentThread().getName() + "  " + a);
    }
}
