package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private static final Logger log = LoggerFactory.getLogger(MealService.class);

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        log.info("save meal, userId - {}, meal -{}", userId, meal);
        return repository.save(userId, meal);
    }

    public Meal update(int userId, Meal meal) {
        log.info("update meal, userId - {}, meal -{}", userId, meal);
        return checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

    public void delete(int userId, int id) {
        log.info("delete meal, userId - {}, mealId -{}", userId, id);
        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public Meal get(int userId, int id) {
        log.info("get meal, userId - {}, mealId -{}", userId, id);
        return checkNotFoundWithId(repository.get(userId, id), id);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        log.info("get all mealTos, userId - {}, user caloriesPerDay -{}", userId, caloriesPerDay);
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> getBetween(int userId, int caloriesPerDay, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("get all mealTos, userId - {}, user caloriesPerDay -{}, input filter params: " +
                        "startDate - {}, endDate - {}, startTime -{}, endTime -{}",
                userId, caloriesPerDay, startDate, endDate, startTime, endTime);
        List<Meal> mealsByDay = repository.getBetween(userId,
                Optional.ofNullable(startDate).orElse(LocalDate.MIN),
                Optional.ofNullable(endDate).orElse(LocalDate.MAX));
        return MealsUtil.getFilteredTos(mealsByDay, caloriesPerDay,
                Optional.ofNullable(startTime).orElse(LocalTime.MIN),
                Optional.ofNullable(endTime).orElse(LocalTime.MAX));
    }
}