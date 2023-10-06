package ru.javawebinar.topjava.web;

import org.apache.commons.lang3.StringUtils;
import ru.javawebinar.topjava.dao.MealCrud;
import ru.javawebinar.topjava.dao.MealCrudImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MealServlet extends HttpServlet {

    private final int MAX_CALORIES = 2000;
    private final String MEAL_LIST_JSP = "/mealList.jsp";
    private final String SAVE_MEAL_JSP = "/saveMeal.jsp";
    private final String MEAL_ID = "mealId";
    private final String MEAL_SERVLET = "meals";
    private MealCrud mealCrud;

    public MealServlet() {
        super();
        this.mealCrud = new MealCrudImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectTo = MEAL_LIST_JSP;
        String action = Optional.ofNullable(req.getParameter("action")).map(String::toLowerCase).orElse(StringUtils.EMPTY);

        switch (action) {
            case "delete":
                String id = req.getParameter(MEAL_ID);
                mealCrud.delete(Integer.parseInt(id));
                getAllMeals(req);
                resp.sendRedirect(MEAL_SERVLET);
                return;
            case "edit":
                String oldId = req.getParameter(MEAL_ID);
                Meal oldMeal = mealCrud.getById(Integer.parseInt(oldId));
                req.setAttribute("meal", oldMeal);
            case "add":
                redirectTo = SAVE_MEAL_JSP;
                RequestDispatcher updateView = req.getRequestDispatcher(redirectTo);
                updateView.forward(req, resp);
                break;
            default:
                getAllMeals(req);
                RequestDispatcher defaultView = req.getRequestDispatcher(redirectTo);
                defaultView.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = Optional.ofNullable(req.getParameter(MEAL_ID)).filter(StringUtils::isNotBlank).map(Integer::parseInt).orElse(null);
        if (Objects.isNull(id)) {
            LocalDateTime dateTime = Optional.ofNullable(req.getParameter("dateTime")).filter(StringUtils::isNotBlank).map(LocalDateTime::parse).orElseGet(LocalDateTime::now);
            String description = req.getParameter("description");
            Integer calories = Optional.ofNullable(req.getParameter("calories")).filter(StringUtils::isNotBlank).map(Integer::parseInt).orElse(0);
            Meal meal = new Meal(dateTime, description, calories);
            mealCrud.save(meal);
        } else {
            Meal oldMeal = mealCrud.getById(id);
            LocalDateTime dateTime = Optional.ofNullable(req.getParameter("dateTime")).filter(StringUtils::isNotBlank).map(LocalDateTime::parse).orElseGet(oldMeal::getDateTime);
            String description = Optional.ofNullable(req.getParameter("description")).orElseGet(oldMeal::getDescription);
            Integer calories = Optional.ofNullable(req.getParameter("calories")).filter(StringUtils::isNotBlank).map(Integer::parseInt).orElseGet(oldMeal::getCalories);
            Meal newMeal = new Meal(oldMeal.getId(), dateTime, description, calories);
            mealCrud.save(newMeal);
        }
        resp.sendRedirect(MEAL_SERVLET);
    }

    private void getAllMeals(HttpServletRequest req) {
        List<MealTo> mealsWithExceed = MealsUtil.filteredByStreams(mealCrud.getAll(), LocalTime.MIN, LocalTime.MAX, MAX_CALORIES);
        req.setAttribute(MEAL_SERVLET, mealsWithExceed);
    }
}