package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL_ID = START_SEQ + 3;
    public static final int UPDATED_MEAL_ID = START_SEQ + 4;

    public static final Meal user_meal = new Meal(MEAL_ID, LocalDateTime.parse("2023-10-01T10:00:00"), "Завтрак", 500);

    public static final List<Meal> meals = Arrays.asList(
            new Meal(MEAL_ID + 9, LocalDateTime.of(2023, Month.OCTOBER, 5, 20, 0), "Ужин админа", 300),
            new Meal(MEAL_ID + 8, LocalDateTime.of(2023, Month.OCTOBER, 5, 13, 0), "Обед админа", 1500),
            new Meal(MEAL_ID + 7, LocalDateTime.of(2023, Month.OCTOBER, 5, 10, 0), "Завтрак админа", 1000)
    );

    public static Meal getUpdated() {
        Meal updated = new Meal(user_meal);
        updated.setId(UPDATED_MEAL_ID);
        updated.setCalories(777);
        updated.setDateTime(LocalDateTime.parse("2023-09-01T09:00:00"));
        updated.setDescription("updated meal");
        return updated;
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.parse("2023-09-30T18:00:00"), "Новая тестовая еда", 444);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("user_id").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("user_id").isEqualTo(expected);
    }
}
