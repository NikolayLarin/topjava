package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.Storage.MealsConcurrentMapStorage;
import ru.javawebinar.topjava.Storage.Storage;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static Storage storage = new MealsConcurrentMapStorage();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(request.getParameter("id"));
        String description = request.getParameter("description");
        if (!description.trim().isEmpty() && id == -1) {
            storage.save(create(description, id, request));
        } else if (!description.trim().isEmpty()) {
            storage.update(create(description, id, request));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action");
        if (action == null) {
            List<MealTo> mealTos;
            if (storage.size() == 0) {
                storage.clear();
                List<Meal> meals = MealsUtil.MEAL_LIST.stream()
                        .map(this::save)
                        .collect(Collectors.toList());
                mealTos = getTos(meals);
            } else {
                mealTos = getTos(storage.getAll());
            }
            forward(mealTos, request, response);
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        switch (action) {
            case "delete":
                storage.delete(id);
                response.sendRedirect("meals");
                break;
            case "edit":
                request.setAttribute("id", id);
                forward(request, response);
                break;
            case "add":
                request.setAttribute("id", -1);
                forward(request, response);
                break;
        }
    }

    private Meal save(Meal meal) {
        storage.save(meal);
        return storage.get(meal.getId());
    }

    private void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("meals", getTos(storage.getAll()));
        request.setAttribute("formatter", MealsUtil.FORMATTER);
        request.getRequestDispatcher("mealsEdit.jsp").forward(request, response);
    }

    private void forward(List<MealTo> mealTos, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("meals", mealTos);
        request.setAttribute("formatter", MealsUtil.FORMATTER);
        request.getRequestDispatcher("mealsView.jsp").forward(request, response);
    }

    private List<MealTo> getTos(List<Meal> meals) {
        return MealsUtil.getFiltered(
                meals, LocalTime.MIN, LocalTime.MAX, MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    private Meal create(String description, int id, HttpServletRequest request) {
        return new Meal(LocalDateTime.parse(request.getParameter("dateTime"), MealsUtil.FORMATTER),
                description, Integer.parseInt((request.getParameter("calories"))), id);
    }
}