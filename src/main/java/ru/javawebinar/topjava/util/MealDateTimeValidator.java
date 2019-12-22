package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MealDateTimeValidator implements Validator {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MessageSource messageSource;

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
                throw new DataIntegrityViolationException(messageSource.getMessage(
                        "exception.duplicate_dateTime", null, LocaleContextHolder.getLocale()));
            }
        }
    }
}