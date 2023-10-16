package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("save meal - {}", meal);
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public Meal update(Meal meal, int id) {
        log.info("update meal - {}", meal);
        assureIdConsistent(meal, id);
        return service.update(authUserId(), meal);
    }

    public void delete(int id) {
        log.info("delete meal with id - {}", id);
        service.delete(authUserId(), id);
    }

    public Meal get(int id) {
        log.info("get meal with id - {}", id);
        return service.get(authUserId(), id);
    }

    public List<MealTo> getAll() {
        log.info("get all mealTos");
        return service.getAll(authUserId(), authUserCaloriesPerDay());
    }

    public List<MealTo> getBetween(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("get all mealTos, input filter params: " +
                "startDate - {}, endDate - {}, startTime -{}, endTime -{}", startDate, endDate, startTime, endTime);
        return service.getBetween(authUserId(), authUserCaloriesPerDay(),
                startDate, endDate, startTime, endTime);
    }
}