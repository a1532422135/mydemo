package com.example.test.effectivejava.staticfactory;

public class Test {
    public static void main(String[] args) {
        final RandomIntGenerator randomIntGenerator = RandomIntGenerator.between(100, 102);
        System.out.println(randomIntGenerator.getRandomInt());
        StringBuilder stringBuilder = new StringBuilder(200);
        stringBuilder.append("111").toString();
        anInt(randomIntGenerator);
        System.out.println(randomIntGenerator.getRandomInt());
    }

    private static void anInt(RandomIntGenerator randomIntGenerator) {
        randomIntGenerator.setMin(200);
        randomIntGenerator.setMax(202);
    }
}
