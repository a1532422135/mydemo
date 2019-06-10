package com.example.test.lock.mysynchronized;

public class SynchronizedThread {
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread().getName()
                        + " synchronized loop " + i);
            }
        }
    }

    public synchronized void run1(){
        for (int i = 0; i < 50; i++) {
            System.out.println(Thread.currentThread().getName()
                    + " synchronized loop " + i);
        }
    }

    public static void main(String[] args) {
        SynchronizedThread t1 = new SynchronizedThread();
        Thread ta = new Thread(new Runnable() {
            @Override
            public void run() {
                t1.run();
            }
        }, "A");
        Thread tb = new Thread(new Runnable() {
            @Override
            public void run() {
                t1.run1();
            }
        }, "B");
        ta.start();
        tb.start();
    }
}
