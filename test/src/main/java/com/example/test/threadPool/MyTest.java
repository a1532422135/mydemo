package com.example.test.threadPool;

import com.example.test.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public  class MyTest extends Test {
    public static void main(String[] args) {
        System.out.println(new Random().nextInt(100000));
    }

    public int main11(String[] args) {
        return 1;
    }
}
