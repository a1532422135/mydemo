package lambdasinaction.chap5;

import lambdasinaction.chap4.Dish;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static lambdasinaction.chap4.Dish.menu;

@Slf4j
public class Finding {

    public static void main(String... args) {
        if (isVegetarianFriendlyMenu()) {
            System.out.println("Vegetarian friendly");
        }

        String a = "a";
        String ac = a + "c";
        String ac2 = "a" + "c";
        log.info("ac==ac2:{}", ac == ac2);

        System.out.println(isHealthyMenu());
        System.out.println(isHealthyMenu2());

        Optional<Dish> dish = findVegetarianDish();
        dish.ifPresent(d -> System.out.println(d.getName()));
    }

    private static boolean isVegetarianFriendlyMenu() {
        return menu.stream().anyMatch(Dish::isVegetarian);
    }

    private static boolean isHealthyMenu() {
        return menu.stream().allMatch(d -> d.getCalories() < 1000);
    }

    private static boolean isHealthyMenu2() {
        return menu.stream().noneMatch(d -> d.getCalories() >= 1000);
    }

    private static Optional<Dish> findVegetarianDish() {
        return menu.stream().filter(Dish::isVegetarian).findAny();
    }

}
