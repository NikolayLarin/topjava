package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.Storage.MealsConcurrentMapStorage;
import ru.javawebinar.topjava.Storage.Storage;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private final Storage storage = new MealsConcurrentMapStorage();

    {
        MealsUtil.MEAL_LIST.forEach(storage::save);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int id = getId(request);
        Meal meal = new Meal(LocalDateTime.parse(
                request.getParameter("dateTime"), MealsUtil.FORMATTER),
                request.getParameter("description"),
                Integer.parseInt((request.getParameter("calories"))), id);
        if (id == -1) {
            storage.save(meal);
        } else {
            storage.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String s = request.getParameter("action");
        String action = (s != null) ? s : "";
        switch (action) {
            case "delete":
                storage.delete(getId(request));
                response.sendRedirect("meals");
                break;
            case "edit":
                int id = getId(request);
                request.setAttribute("id", id);
                request.setAttribute("meal", storage.get(id));
                forward("mealsEdit.jsp", request, response);
                break;
            case "add":
                request.setAttribute("id", -1);
                request.setAttribute("meal", new Meal(
                        LocalDateTime.of(1, 1, 1, 0, 1),
                        "", 0));
                forward("mealsEdit.jsp", request, response);
                break;
            default:
                request.setAttribute("meals", MealsUtil.getFiltered(
                        storage.getAll(), LocalTime.MIN, LocalTime.MAX,
                        MealsUtil.DEFAULT_CALORIES_PER_DAY));
                forward("mealsView.jsp", request, response);
                break;
        }

    }

    private int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }

    private void forward(String jsp, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("formatter", MealsUtil.FORMATTER);
        request.getRequestDispatcher(jsp).forward(request, response);
    }
}