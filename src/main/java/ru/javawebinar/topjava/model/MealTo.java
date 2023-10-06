package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealTo {

    private final Integer id;
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean excess;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = IdGenerator.nextId();
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public MealTo(Meal meal, boolean excess) {
        this.id = Objects.isNull(meal.getId()) ? IdGenerator.nextId() : meal.getId();
        this.dateTime = meal.getDateTime();
        this.description = meal.getDescription();
        this.calories = meal.getCalories();
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

    public LocalDate getLocalDate() {
        return dateTime.toLocalDate();
    }
}
