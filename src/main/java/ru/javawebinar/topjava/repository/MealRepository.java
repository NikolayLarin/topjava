package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal, int userId);

    // false if not found
    boolean delete(int mealId, int userId);

    // null if not found
    Meal get(int mealId, int userId);

    Collection<Meal> getAll();

    Collection<Meal> getAllByUser(int userId);

    Collection<Meal> getAllByUserByDates(int userId, LocalDate startDate, LocalDate endDate);
}