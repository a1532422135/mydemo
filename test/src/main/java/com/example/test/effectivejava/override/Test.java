package com.example.test.effectivejava.override;


import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        a(1);
    }

    public enum Days {
        MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);
        private int day;

        Days(int day) {
            this.day = day;
        }
    }

    private static void a(int i) {
        System.out.println(Days.FRIDAY.day);
    }

    protected static int a() {
        return 1;
    }
}
