package com.example.test.lock;

public class Test {
    public static void main(String[] args) {
        Print p = new Print();
        new Thread(new Runnable() {

            @Override
            public void run() {
                p.printNum();
            }
        }).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                p.printWord();
            }
        }).start();
    }
}

class Print {
    char chr = 'A';
    int num = 1;

    public synchronized void printNum() {
        System.out.print(num);
        num += 1;
        notify();
        try {
            wait();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

    }

    public synchronized void printWord() {
        System.out.print(chr);
        chr += 1;
        notify();
        try {
            if (chr <= 'Z')
                wait();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
