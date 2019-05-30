package com.example.test.lock;

import java.util.concurrent.atomic.AtomicReference;

public class MyAtomic {

    public static void main(String[] args) {
        People people1 = new People("Bom", 0);
        People people2 = new People("Tom", 10);
        //先初始化一个值，如果不初始化则默认值为null
        AtomicReference<People> reference = new AtomicReference<>(people1);
        People people3 = reference.get();
        if (people3.equals(people1)) {
            System.out.println("people3:" + people3);
        } else {
            System.out.println("else:" + people3);
        }
        /**
         *当前值：拿当前值和reference.get() 获取到的值去比较，如果相等则true并更新值为期望值
         * 期望值：如果返回true则更新为期望值，如果返回false则不更新值
         */
        boolean b = reference.compareAndSet(null, people2);
        System.out.println("myClass.main-" + b + "--" + reference.get());
        boolean b1 = reference.compareAndSet(people1, people2);
        System.out.println("myClass.main-" + b1 + "--" + reference.get());
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread1-----------");
                People people = reference.get();
                people.setName("Tom1");
                people.setAge(people.getAge() + 1);
                reference.getAndSet(people);
                System.out.println("Thread1:" + reference.get().toString());
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread2-----------");
                People people = reference.get();
                people.setName("Tom2");
                people.setAge(people.getAge() + 1);
                reference.getAndSet(people);
                System.out.println("Thread2:" + reference.get().toString());
            }
        }).start();
    }

    public static class People {
        private String name;
        private int age;

        public People(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "People{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
