package com.example.test.effectivejava.override;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Administrator
 */
public class Bigram {
    private char first = 'a';
    private char second = 'b';

    public void sout(String a) {
        System.out.println(a);
        System.out.println(a);
    }

    public Bigram() {
        System.out.println("bigram");
    }

    public Bigram(char first, char second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Bigram)) {
            return false;
        }
        Bigram b = (Bigram) o;
        return b.first == first && b.second == second;
    }

    @Override
    public int hashCode() {
        return 31 * first + second;
    }

    public static void main(String[] args) {
        Set<Bigram> s = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            for (char ch = 'a'; ch <= 'z'; ch++) {
                s.add(new Bigram(ch, ch));
            }
        }
        System.out.println(s.size());
    }
}