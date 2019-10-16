package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController extends AbstractMealController {

    public List<MealTo> getAll() {
        return super.getAll(authUserId(), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return super.getAllFiltered(authUserId(), authUserCaloriesPerDay(), startDate, endDate, startTime, endTime);
    }

    public Meal get(int id) {
        return super.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        return super.create(meal, authUserId());
    }

    public void delete(int id) {
        super.delete(id, authUserId());
    }

    public void update(Meal meal) {
        super.update(meal, meal.getId(), authUserId());
    }
}