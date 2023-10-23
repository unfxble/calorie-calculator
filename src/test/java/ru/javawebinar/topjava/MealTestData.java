package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int USER_START_MEAL_ID = START_SEQ + 3;
    public static final int USER_UPDATED_MEAL_ID = START_SEQ + 4;

    public static final Meal userMeal = new Meal(USER_START_MEAL_ID, LocalDateTime.of(2023, Month.OCTOBER, 1, 10, 0), "Завтрак", 500);

    public static final List<Meal> adminMeals = Arrays.asList(
            new Meal(USER_START_MEAL_ID + 9, LocalDateTime.of(2023, Month.OCTOBER, 5, 20, 0), "Ужин админа", 300),
            new Meal(USER_START_MEAL_ID + 8, LocalDateTime.of(2023, Month.OCTOBER, 5, 13, 0), "Обед админа", 1500),
            new Meal(USER_START_MEAL_ID + 7, LocalDateTime.of(2023, Month.OCTOBER, 5, 10, 0), "Завтрак админа", 1000)
    );

    public static final List<Meal> userMealsInclusive = Arrays.asList(
            new Meal(USER_START_MEAL_ID + 6, LocalDateTime.of(2023, Month.OCTOBER, 15, 19, 0), "Ужин", 410),
            new Meal(USER_START_MEAL_ID + 5, LocalDateTime.of(2023, Month.OCTOBER, 15, 14, 0), "Обед", 500),
            new Meal(USER_START_MEAL_ID + 4, LocalDateTime.of(2023, Month.OCTOBER, 15, 10, 0), "Завтрак", 1000),
            new Meal(USER_START_MEAL_ID + 3, LocalDateTime.of(2023, Month.OCTOBER, 15, 0, 0), "Граничное значение", 100)
    );

    public static Meal getUpdatedUserMeal() {
        Meal updated = new Meal(userMeal);
        updated.setId(USER_UPDATED_MEAL_ID);
        updated.setCalories(777);
        updated.setDateTime(LocalDateTime.of(2023, Month.SEPTEMBER, 1, 9, 0));
        updated.setDescription("updated meal");
        return updated;
    }

    public static Meal getNewUserMeal() {
        return new Meal(null, LocalDateTime.of(2023, 9, 30, 18, 0), "Новая тестовая еда", 444);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertMatch(Collections.singleton(actual), Collections.singleton(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}