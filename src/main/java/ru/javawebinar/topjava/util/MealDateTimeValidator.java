package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.ExistException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MealDateTimeValidator implements Validator {

    @Autowired
    private MealRepository mealRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Meal.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Meal meal = (Meal) target;

        Map<String, LocalDateTime> meals = new ConcurrentHashMap<>();

        LocalDateTime mealLtd = meal.getDateTime();
        if (mealLtd != null) {
            for (Meal m : mealRepository.getAll(SecurityUtil.authUserId())) {
                LocalDateTime ldt = m.getDateTime();
                meals.put(ldt.toString(), ldt);
            }
            if (meals.get(mealLtd.toString()) != null) {
                throw new ExistException("You already have Meal at this dateTime");
            }
        }
    }
}