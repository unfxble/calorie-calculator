package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.FilterTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    // <userId, <mealId, Meal>>
    private final Map<Integer, Map<Integer, Meal>> userMealRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(1, meal));
        MealsUtil.adminMeals.forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("userId: {}, save meal: {}", userId, meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            return userMealRepository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                    .put(meal.getId(), meal);
        }
        // handle case: update, but not present in storage
        return Optional.ofNullable(userMealRepository.getOrDefault(userId, null))
                .map(meals -> meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal))
                .orElse(null);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("Delete meal with id: {}", id);
        return Optional.ofNullable(userMealRepository.get(userId))
                .map(meal -> meal.remove(id))
                .isPresent();
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("Get meal with id: {}", id);
        return Optional.ofNullable(userMealRepository.get(userId))
                .map(meals -> meals.get(id))
                .orElse(null);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        log.info("userId: {}, get all meals", userId);
        return Optional.ofNullable(userMealRepository.get(userId))
                .map(Map::values)
                .map(meals -> meals.stream()
                        .sorted(Comparator.comparing(Meal::getDate).reversed())
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    @Override
    public Collection<Meal> getBetween(Integer userId, FilterTo filter) {

        LocalDate startDate = Optional.ofNullable(filter.getStartDate()).filter(this::isNotBlank).map(LocalDate::parse).orElse(LocalDate.MIN);
        LocalDate endDate = Optional.ofNullable(filter.getEndDate()).filter(this::isNotBlank).map(LocalDate::parse).orElse(LocalDate.MAX);

        LocalTime startTime = Optional.ofNullable(filter.getStartTime()).filter(this::isNotBlank).map(LocalTime::parse).orElse(LocalTime.MIN);
        LocalTime endTime = Optional.ofNullable(filter.getEndTime()).filter(this::isNotBlank).map(LocalTime::parse).orElse(LocalTime.MAX);

        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .collect(Collectors.toList());
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}

