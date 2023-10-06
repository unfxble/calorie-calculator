package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MealCrudImpl implements MealCrud{

    private final Map<Integer, Meal> mealMap;

    public MealCrudImpl() {
        this.mealMap = new ConcurrentHashMap<>();
        Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ).forEach(meal -> mealMap.put(meal.getId(), meal));
    }

    @Override
    public void save(Meal meal) {
        mealMap.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }

    @Override
    public Meal getById(int id) {
        return mealMap.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }
}