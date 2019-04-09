package com.example.test.effectivejava.builder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * @author Administrator
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;


    @Override
    public String toString() {
        return " " + this.calories + " " + this.carbohydrate + " " + this.calories + " " + this.servings + " " + this.sodium + " " + this.servingSize;
    }
}
