package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
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
        log.info("InMemoryMealRepository:: userId - {}, save meal - {}", userId, meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMealRepository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return Optional.ofNullable(userMealRepository.get(userId))
                .map(meals -> meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal))
                .orElse(null);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("InMemoryMealRepository:: delete meal with id - {}", id);
        return Optional.ofNullable(userMealRepository.get(userId))
                .map(mealMap -> mealMap.remove(id))
                .isPresent();
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("InMemoryMealRepository:: get meal with id - {}", id);
        return Optional.ofNullable(userMealRepository.get(userId))
                .map(meals -> meals.get(id))
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("InMemoryMealRepository:: userId - {}, get all meals", userId);
        return filterByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetween(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("InMemoryMealRepository:: userId - {}, get all meals with filter startDate - {}, endDate - {}",
                userId, startDate, endDate);
        return filterByPredicate(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate));
    }

    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        return Optional.ofNullable(userMealRepository.get(userId))
                .map(Map::values)
                .map(meals -> meals.stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }
}

