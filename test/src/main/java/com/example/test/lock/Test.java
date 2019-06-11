package com.example.test.lock;

public class Test {
    public static void main(String[] args) {
        Print p = new Print();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    p.printNum();
                }
            }
        }).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    p.printWord();
                }
            }
        }).start();
    }
}

class Print {
    char chr = 'A';
    int num = 1;

    public synchronized void printNum() {
        if (num > 9) {
            num = 1;
        }
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
        if (chr > 'Z') {
            chr = 'A';
        }
        System.out.print(chr);
        chr += 1;
        notify();
        try {
            wait();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
