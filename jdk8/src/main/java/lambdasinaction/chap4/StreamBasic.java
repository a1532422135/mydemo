package lambdasinaction.chap4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class StreamBasic {
    private static List<Integer> list = new CopyOnWriteArrayList<Integer>();
    private static Lock lock = new ReentrantLock();

    public static void main(String... args) {
        // Java 7
        getLowCaloricDishesNamesInJava7(Dish.menu).forEach(System.out::println);

        System.out.println("---");

        // Java 8
        getLowCaloricDishesNamesInJava8(Dish.menu).forEach(System.out::println);
        IntStream.range(0, 10000).parallel().forEach(e -> {
//            lock.lock();
            list.add(e);
//            lock.unlock();
        });
        System.out.println(list.size());
    }

    public static List<String> getLowCaloricDishesNamesInJava7(List<Dish> dishes) {
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for (Dish d : dishes) {
            if (d.getCalories() <= 400) {
                lowCaloricDishes.add(d);
            }
        }
        List<String> lowCaloricDishesName = new ArrayList<>();
        Collections.sort(lowCaloricDishes, (d1, d2) -> Integer.compare(d1.getCalories(), d2.getCalories()));
        for (Dish d : lowCaloricDishes) {
            lowCaloricDishesName.add(d.getName());
        }
        return lowCaloricDishesName;
    }

    public static List<String> getLowCaloricDishesNamesInJava8(List<Dish> dishes) {
        Stream stream = dishes.stream();
        stream.forEach(System.out::print);
        return dishes.parallelStream()
                .filter(d -> {
                    System.out.println(Thread.currentThread().getName());
                    return d.getCalories() <= 400;
                })
                .sorted(comparing(Dish::getCalories))
                .map(new Function<Dish, String>() {
                    @Override
                    public String apply(Dish dish) {
                        return dish.getName();
                    }
                })
                .collect(toList());
    }
}
