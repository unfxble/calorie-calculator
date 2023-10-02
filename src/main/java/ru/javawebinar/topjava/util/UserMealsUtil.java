package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {

    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        filteredByCycles(meals, LocalTime.of(12, 0), LocalTime.of(20, 0), 2000)
                .forEach(System.out::println);

        filteredByStreams(meals, LocalTime.of(13, 0), LocalTime.of(20, 0), 2000)
                .forEach(System.out::println);

        filterByCollector(meals, LocalTime.of(12, 0), LocalTime.of(20, 0), 2000)
                .forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesByDay = new HashMap<>();
        for (UserMeal meal : meals) {
            totalCaloriesByDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(toUserMealWithExcess(meal, totalCaloriesByDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesByDay = meals.stream()
                .collect(Collectors.toMap(meal -> meal.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> toUserMealWithExcess(meal, totalCaloriesByDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filterByCollector(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        final class UserMealCollector implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>> {

            final Map<LocalDate, Integer> totalCaloriesByDay = new ConcurrentHashMap<>();

            @Override
            public Supplier<List<UserMeal>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
                return (userMealList, userMeal) -> {
                    totalCaloriesByDay.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
                    if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                        userMealList.add(userMeal);
                    }
                };
            }

            @Override
            public BinaryOperator<List<UserMeal>> combiner() {
                // используется в параллельных вычислениях
                return (a, b) -> {
                    throw new UnsupportedOperationException();
                };
            }

            @Override
            public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
                return userMeals -> userMeals.stream()
                        .map(meal -> toUserMealWithExcess(meal, totalCaloriesByDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                        .collect(Collectors.toList());
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.emptySet();
            }
        }

        return meals.stream().collect(new UserMealCollector());
    }

    private static UserMealWithExcess toUserMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}