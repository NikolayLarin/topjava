package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
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
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final int DEFAULT_CALORIES_PER_DAY = 2000;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");

    private static final Meal MEAL_1 =
            new Meal(LocalDateTime.of(2019, Month.MAY, 30, 10, 0), "Завтрак", 500);
    private static final Meal MEAL_2 =
            new Meal(LocalDateTime.of(2019, Month.MAY, 30, 13, 0), "Обед", 1000);
    private static final Meal MEAL_3 =
            new Meal(LocalDateTime.of(2019, Month.MAY, 30, 20, 0), "Ужин", 500);
    private static final Meal MEAL_4 =
            new Meal(LocalDateTime.of(2019, Month.MAY, 31, 10, 0), "Завтрак", 1000);
    private static final Meal MEAL_5 =
            new Meal(LocalDateTime.of(2019, Month.MAY, 31, 13, 0), "Обед", 500);
    private static final Meal MEAL_6 =
            new Meal(LocalDateTime.of(2019, Month.MAY, 31, 20, 0), "Ужин", 510);

    private static final List<Meal> MEAL_LIST = Arrays.asList(MEAL_3, MEAL_5, MEAL_1, MEAL_4, MEAL_2, MEAL_6);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        List<MealTo> mealToList = MealsUtil.getFiltered(MEAL_LIST, LocalTime.MIN, LocalTime.MAX, DEFAULT_CALORIES_PER_DAY);
        request.setAttribute("mealList", mealToList);
        request.setAttribute("formatter", FORMATTER);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
        response.sendRedirect("meals.jsp");
    }
}