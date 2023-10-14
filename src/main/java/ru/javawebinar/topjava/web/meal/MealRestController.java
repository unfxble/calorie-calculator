package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.FilterTo;
import ru.javawebinar.topjava.to.MealTo;

import java.util.Collection;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        return service.create(authUserId(), meal);
    }

    public Meal update(Meal meal) {
        return service.update(authUserId(), meal);
    }

    public void delete(int id) {
        service.delete(authUserId(), id);
    }

    public Meal get(int id) {
        return service.get(authUserId(), id);
    }

    public Collection<MealTo> getAll() {
        return service.getAll(authUserId(), authUserCaloriesPerDay());
    }

    public Collection<MealTo> getBetween(FilterTo filter) {
        return service.getBetween(authUserId(), authUserCaloriesPerDay(), filter);
    }

}