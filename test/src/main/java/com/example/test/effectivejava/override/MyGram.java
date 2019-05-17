package com.example.test.effectivejava.override;

public class MyGram extends Bigram {

    public MyGram(){
        Test test = new Test();
        test.a();
        System.out.println("mygram");
    }

    @Override
    public void sout(String a) {
        System.out.println(a);
    }
}
