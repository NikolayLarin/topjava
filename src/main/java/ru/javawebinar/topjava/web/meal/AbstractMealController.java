package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenInclusive;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public class AbstractMealController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll(int userId, int userCaloriesPerDay) {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(userId), userCaloriesPerDay);
    }

    public List<MealTo> getAllFiltered(
            int userId, int caloriesPerDay,
            LocalDate startDate, LocalDate endDate,
            LocalTime startTime, LocalTime endTime) {
        log.info("getAllByDatesAndTime");
        return getFilteredTos(
                service.getAllByDates(userId, startDate, endDate)
                        .stream()
                        .filter(meal -> isBetweenInclusive(meal.getTime(), startTime, endTime))
                        .collect(Collectors.toList()),
                caloriesPerDay, startTime, endTime);
    }

/*
    public List<MealTo> getAllByDatesAndTime(String startDate, String endDate, String startTime, String endTime) {
        log.info("getAllByDatesAndTime");
        return getFilteredTos(
                service.getAllByDates(authUserId(), parse(startDate, START_DATE), parse(endDate, END_DATE))
                        .stream()
                        .filter(meal -> isBetweenInclusive(
                                meal.getTime(), parse(startTime, START_TIME), parse(endTime, END_TIME)))
                        .collect(Collectors.toList()),
                authUserCaloriesPerDay(),
                parse(startTime, START_TIME),
                parse(endTime, TimeTypeUtil.END_TIME));
    }
*/


    public Meal get(int id, int userId) {
        log.info("get {}", id);
        return service.get(id, userId);
    }

    public Meal create(Meal meal, int userId) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id, int userId) {
        log.info("delete {}", id);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id, int userId) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }
}