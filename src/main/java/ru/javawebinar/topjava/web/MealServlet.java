package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.DateTypeUtil.END_DATE;
import static ru.javawebinar.topjava.util.DateTimeUtil.DateTypeUtil.START_DATE;
import static ru.javawebinar.topjava.util.DateTimeUtil.TimeTypeUtil.END_TIME;
import static ru.javawebinar.topjava.util.DateTimeUtil.TimeTypeUtil.START_TIME;
import static ru.javawebinar.topjava.util.DateTimeUtil.parse;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext appCtx =
            new ClassPathXmlApplicationContext("spring/spring-app.xml");
    private MealRestController mealRestController =
            appCtx.getBean(MealRestController.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            mealRestController.create(meal);
        } else {
            mealRestController.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            default:
                final String sd = setValue("startDate", request);
                final String ed = setValue("endDate", request);
                final String st = setValue("startTime", request);
                final String et = setValue("endTime", request);
                if ((sd.trim() + ed.trim() + st.trim() + et.trim()).isEmpty()) {
                    log.info("getAll");
                    request.setAttribute("meals", mealRestController.getAll());
                } else {
                    log.info("getAllFiltered");
                    request.setAttribute("meals", mealRestController.getAllFiltered(
                            parse(sd, START_DATE), parse(ed, END_DATE),
                            parse(st, START_TIME), parse(et, END_TIME)));
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private String setValue(String name, HttpServletRequest request) {
        String value = request.getParameter(name);
        return value != null ? value : "";
    }
}