package com.example.test.effectivejava.staticfactory;

import lombok.Setter;

import java.util.Random;

/**
 * @author Administrator
 */
public class RandomIntGenerator {
    @Setter
    private int min;
    @Setter
    private int max;

    private RandomIntGenerator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static RandomIntGenerator between(int min, int max) {
        return new RandomIntGenerator(min, max);
    }

    public static RandomIntGenerator biggerThan(int min) {
        return new RandomIntGenerator(min, Integer.MAX_VALUE);
    }

    public static RandomIntGenerator smallerThan(int max) {
        return new RandomIntGenerator(0, max);
    }

    public int getRandomInt() {
        Random random = new Random();
        return random.nextInt(this.max - this.min) + this.min;
    }
}
