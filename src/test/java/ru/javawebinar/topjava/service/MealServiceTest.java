package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static ru.javawebinar.topjava.MealTestData.USER_START_MEAL_ID;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.getNewUserMeal;
import static ru.javawebinar.topjava.MealTestData.getUpdatedUserMeal;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal userMeal = mealService.get(USER_START_MEAL_ID, USER_ID);
        assertMatch(userMeal, MealTestData.userMeal);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(USER_START_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getNotExists() {
        assertThrows(NotFoundException.class, () -> mealService.get(START_SEQ - 1, USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(USER_START_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(USER_START_MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNotExists() {
        assertThrows(NotFoundException.class, () -> mealService.delete(START_SEQ - 1, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(USER_START_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusiveFound() {
        List<Meal> meals = mealService.getBetweenInclusive(LocalDate.of(2023, Month.OCTOBER, 15), LocalDate.of(2023, Month.OCTOBER, 15), USER_ID);
        assertMatch(meals, MealTestData.userMealsInclusive);
    }

    @Test
    public void getBetweenInclusiveEmpty() {
        List<Meal> meals = mealService.getBetweenInclusive(LocalDate.of(2023, Month.MAY, 1), LocalDate.of(2023, Month.MAY, 1), USER_ID);
        assertTrue(meals.isEmpty());
    }

    @Test
    public void getAll() {
        List<Meal> adminMeals = mealService.getAll(ADMIN_ID);
        assertMatch(adminMeals, MealTestData.adminMeals);
    }

    @Test
    public void update() {
        Meal userMeal = getUpdatedUserMeal();
        mealService.update(userMeal, USER_ID);
        assertMatch(mealService.get(userMeal.getId(), USER_ID), getUpdatedUserMeal());
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.update(getUpdatedUserMeal(), ADMIN_ID));
    }

    @Test
    public void create() {
        Meal createdUserMeal = mealService.create(getNewUserMeal(), USER_ID);
        Integer newUserMealId = createdUserMeal.getId();
        Meal newUserMeal = getNewUserMeal();
        newUserMeal.setId(newUserMealId);
        assertMatch(createdUserMeal, newUserMeal);
        assertMatch(mealService.get(newUserMealId, USER_ID), newUserMeal);
    }

    @Test
    public void createDuplicateDateTime() {
        mealService.create(getNewUserMeal(), USER_ID);
        assertThrows(DataAccessException.class, () -> mealService.create(getNewUserMeal(), USER_ID));
    }
}