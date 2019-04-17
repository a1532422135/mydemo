package lambdasinaction.chap3;

import java.util.*;
import java.util.function.Predicate;

public class Lambdas {
	public static void main(String ...args){// Filtering with lambdas
        List<Apple> inventory = Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(160, "red"));

		// Simple example
		Runnable r = () -> System.out.println("Hello!");
		r.run();

		Comparator<Apple> c = Comparator.comparing(Apple::getWeight);
        inventory.sort(c.reversed().thenComparing(Apple::getColor));

        Predicate<Apple> redApple = Apple::isRed;

        redApple = redApple.and(apple -> apple.getWeight() > 150).or(apple -> "green".equals(apple.getColor()));

		// [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
		List<Apple> greenApples = filter(inventory,redApple);
		System.out.println(greenApples);

		inventory.sort(c);
		System.out.println(inventory);
	}

	public static List<Apple> filter(List<Apple> inventory, Predicate<Apple> p){
		List<Apple> result = new ArrayList<>();
		for(Apple apple : inventory){
			if(p.test(apple)){
				result.add(apple);
			}
		}
		return result;
	}

	public static class Apple {
		private int weight = 0;
		private String color = "";

		public Apple(int weight, String color){
			this.weight = weight;
			this.color = color;
		}

		public Integer getWeight() {
			return weight;
		}

		public void setWeight(Integer weight) {
			this.weight = weight;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public boolean isRed(){
		    return "red".equalsIgnoreCase(this.color);
        }

		public String toString() {
			return "Apple{" +
					"color='" + color + '\'' +
					", weight=" + weight +
					'}';
		}
	}

	interface ApplePredicate{
		public boolean test(Apple a);
	}
}