package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.MealCrud;
import ru.javawebinar.topjava.dao.InMemoryMealCrud;
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
import java.util.Optional;

public class MealServlet extends HttpServlet {

    private final int MAX_CALORIES = 2000;
    private final String MEAL_LIST_JSP = "/mealList.jsp";
    private final String SAVE_MEAL_JSP = "/saveMeal.jsp";
    private MealCrud mealCrud;

    @Override
    public void init() {
        this.mealCrud = new InMemoryMealCrud();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectTo = MEAL_LIST_JSP;
        String action = Optional.ofNullable(req.getParameter("action")).map(String::toLowerCase).orElse("");

        switch (action) {
            case "delete":
                String id = req.getParameter("mealId");
                mealCrud.delete(Integer.parseInt(id));
                resp.sendRedirect("meals");
                return;
            case "edit":
                Meal oldMeal = Optional.ofNullable(req.getParameter("mealId"))
                        .map(Integer::parseInt)
                        .map(mealCrud::getById)
                        .orElseThrow(() -> new UnsupportedOperationException("Can't edit meal without id"));
                req.setAttribute("meal", oldMeal);
            case "add":
                redirectTo = SAVE_MEAL_JSP;
                RequestDispatcher updateView = req.getRequestDispatcher(redirectTo);
                updateView.forward(req, resp);
                break;
            default:
                List<MealTo> mealsWithExceed = MealsUtil.filteredByStreams(mealCrud.getAll(), LocalTime.MIN, LocalTime.MAX, MAX_CALORIES);
                req.setAttribute("meals", mealsWithExceed);
                RequestDispatcher defaultView = req.getRequestDispatcher(redirectTo);
                defaultView.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        Integer id = Optional.ofNullable(req.getParameter("mealId")).filter(this::isNotBlank).map(Integer::parseInt).orElse(null);
        LocalDateTime dateTime = Optional.ofNullable(req.getParameter("dateTime")).filter(this::isNotBlank).map(LocalDateTime::parse).orElseGet(LocalDateTime::now);
        String description = req.getParameter("description");
        Integer calories = Optional.ofNullable(req.getParameter("calories")).filter(this::isNotBlank).map(Integer::parseInt).orElse(0);
        Meal meal = new Meal(id, dateTime, description, calories);
        mealCrud.save(meal);
        resp.sendRedirect("meals");
    }

    private boolean isNotBlank(String str) {
        return !str.isEmpty() && !str.trim().isEmpty();
    }
}