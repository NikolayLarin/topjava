package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.DateTypeUtil.END_DATE;
import static ru.javawebinar.topjava.util.DateTimeUtil.DateTypeUtil.START_DATE;
import static ru.javawebinar.topjava.util.DateTimeUtil.TimeTypeUtil;
import static ru.javawebinar.topjava.util.DateTimeUtil.TimeTypeUtil.END_TIME;
import static ru.javawebinar.topjava.util.DateTimeUtil.TimeTypeUtil.START_TIME;
import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenInclusive;
import static ru.javawebinar.topjava.util.DateTimeUtil.parse;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllByUser() {
        log.info("getAllByUser");
        return MealsUtil.getTos(service.getAllByUser(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllByUserDatesAndTime(String startDate, String endDate, String startTime, String endTime) {
        log.info("getAllByUserDatesAndTime");
        return getFilteredTos(
                service.getAllByUserByDates(authUserId(), parse(startDate, START_DATE), parse(endDate, END_DATE))
                        .stream()
                        .filter(meal -> isBetweenInclusive(
                                meal.getTime(), parse(startTime, START_TIME), parse(endTime, END_TIME)))
                        .collect(Collectors.toList()),
                authUserCaloriesPerDay(),
                parse(startTime, START_TIME),
                parse(endTime, TimeTypeUtil.END_TIME));
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }
}