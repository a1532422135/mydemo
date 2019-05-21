package com.example.test;

import com.example.test.design.observe.define.WeatherData;
import io.swagger.models.auth.In;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Test {
    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return null;
            }
        });
    }

    public static List<String> main(List<String> list) {
//        Stream.iterate(0,t->t+1).limit(list.size()).map(a->list.stream().collect(Collectors.joining("*"))).forEach(a -> System.out.println(a));
        Object object = list.parallelStream().skip(5000).limit(5000).collect(Collectors.joining(","));
//        System.out.println(object);
        Stream.iterate(0, t -> t + 1).limit(list.size()).map(a -> object).forEach(a -> System.out.println(a));
        return null;
    }

    private List<String> a(List<String> list) {
        List<String> list1 = Stream.iterate(0, t -> t + 1)
                .limit(list.size())
                .parallel()
                .map(a -> list.parallelStream()
                        .skip(a * 5000)
                        .limit(5000)
                        .collect(Collectors.joining(",")))
                .filter(b -> !b.isEmpty()).collect(Collectors.toList());
        return list1;
    }
}
